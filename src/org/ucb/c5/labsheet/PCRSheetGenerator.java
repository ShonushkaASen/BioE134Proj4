package org.ucb.c5.labsheet;

import org.ucb.c5.Inventory.Inventory;
import org.ucb.c5.constructionfile.model.PCR;
import org.ucb.c5.constructionfile.model.Step;

import java.util.*;

/**
 * @author Heloise Carion, Katlyn Ho, Sina Ghandian
**/

public class PCRSheetGenerator {
    private static int current_sample_num; // for labeling purposes
    private static HashSet<String> usedReagents;
    private static StringBuilder samples;
    private static StringBuilder source;
    private static StringBuilder destination;
    private static StringBuilder notes1;
    private static StringBuilder dilutionDests;
    private static StringBuilder notes2;


    public void initiate() throws Exception {
    }

    public static List<String> run(Step step, Inventory inventory, String thread, List<String> currStrings, String header) throws Exception {
         //creating separate strings for each heading within the PCRSheet
         //if currStrings is null, this means this is the first time a PCR step has been encountered
        if (currStrings == null) {
            //since this is the first time through, we are adding headings to each string
            current_sample_num = 0;
            usedReagents = new HashSet<>(); //to ensure we are not putting duplicate info in source
            samples = new StringBuilder();
            samples.append(thread).append(": PCR \n\n").append("samples: \nlabel\tprimer1\tprimer2\ttemplate\tproduct\n");
            source = new StringBuilder();
            source.append("source: \nname\tlocation\tnote\n");
            //destination is an ambiguous thermocycler, hence the placeholder
            destination = new StringBuilder();
            destination.append("destination: thermocycler" + "1A_placeholder\n");
            notes1 = new StringBuilder();
            notes1.append("Note:\nWhen complete, save any dilutions you have made in the locations indicated under dilution destinations\n");
            //dilutionDests contains the location in inventory where the user should put their newly made dilutions
            dilutionDests = new StringBuilder();
            dilutionDests.append("dilution destinations:\n");
            notes2 = new StringBuilder();
            notes2.append("Note:\nNever let enzymes warm up!  Only take the enzyme cooler out of the freezer when you are actively using it, and only take the tubes out of it when actively dispensing.\n");
        } else {
            //since we know the order in which we are returning the strings, we can reassign the stringbuilders accordingly
            samples = new StringBuilder(currStrings.get(0));
            source = new StringBuilder(currStrings.get(1));
            destination = new StringBuilder(currStrings.get(2));
            notes1 = new StringBuilder(currStrings.get(3));
            dilutionDests = new StringBuilder(currStrings.get(4));
            notes2 = new StringBuilder(currStrings.get(5));
        }
        current_sample_num++;
        //casting step into PCR step object
        PCR pcrstep = (PCR) step;
        //extracting info needed for samples
        String label = thread + current_sample_num;
        String oligo1 = pcrstep.getOligo1();
        String oligo2 = pcrstep.getOligo2();
        String template = pcrstep.getTemplate();
        //product name would be for example pTarg2/pcrpdt
        String product = String.format("%s/%s", header, pcrstep.getProduct());
        
        //putting all the above info into samples stringbuilder
        samples.append(label).append("\t").append(oligo1).append("\t")
                .append(oligo2).append("\t").append(template).append('\t').append(product).append("\n");

        //inventory.get() returns a SimpleObject<string,string> where
        //the key is the location and the value would be the dilution note if needed.
        if (!usedReagents.contains(template)) {
            usedReagents.add(template);
            //appending attributes to source in the appropriate order to match with headings
            source.append(template + '\t' + inventory.get(template, -1.0).getKey() +'\t' +
                    inventory.get(template, -1.0).getValue() + '\n');
        }
        if (!usedReagents.contains(oligo1)) {
            usedReagents.add(oligo1);
            source.append(oligo1 + '\t' + inventory.get(oligo1, 10.0).getKey() +'\t' +
                    inventory.get(oligo1, 10.0).getValue() + '\n');
        }
        if (!usedReagents.contains(oligo2)) {
            usedReagents.add(oligo2);
            source.append(oligo2 + '\t' + inventory.get(oligo2, 10.0).getKey() +'\t' +
                    inventory.get(oligo2, 10.0).getValue() + '\n');
        }
        //will have to check if these notes are empty to see if dilution is necessary
        String oligo1Note = inventory.get(oligo1, 10.0).getValue();
        String templateNote = inventory.get(template, -1.0).getValue();
        String oligo2Note = inventory.get(oligo2, 10.0).getValue();
        //if there is a dilution necessary, we must include a dilution note, and figure out where the
        //diluted version of the product will go in inventory, as returned by the put() method
        if (!templateNote.equals("")) {
            //an inventory method that returns the next empty slot in the thread/box of interest
            String templateLoc = inventory.put(template, -1.0, "box" + thread);
            dilutionDests.append(template + '\t' + templateLoc);
        }
        if (!oligo1Note.equals("")) {
            String oligo1Loc = inventory.put(oligo1, 10.0, "box" + thread);
            dilutionDests.append(oligo1 + '\t' + oligo1Loc);
        }
        if (!oligo2Note.equals("")) {
            String oligo2Loc = inventory.put(oligo2, 10.0, "box" + thread);
            dilutionDests.append(oligo2 + '\t' + oligo2Loc);
        }

        //converting Stringbuilders to String and passing them into an array to be returned
        List<String> newCurrStrings = new ArrayList<String>(Arrays.asList(samples.toString(), source.toString(), destination.toString(), notes1.toString(), dilutionDests.toString(), notes2.toString()));
        return newCurrStrings;
    }
}
