package org.ucb.c5.labsheet;

import org.ucb.c5.Inventory.Inventory;
import org.ucb.c5.constructionfile.model.PCR;
import org.ucb.c5.constructionfile.model.Step;

import java.util.*;

public class PCRSheetGenerator {
    private static int current_sample_num;
    private static HashSet<String> usedReagents;
    private static StringBuilder samples;
    private static StringBuilder source;
    private static StringBuilder destination;
    private static StringBuilder notes1;
    private static StringBuilder dilutionDests;


    public void initiate() throws Exception {
        //for labeling purposes
        //to ensure we are not creating multiple sources for the same reagent
    }

    public static List<String> run(Step step, Inventory inventory, String thread, List<String> currStrings, String header) throws Exception {
         //creating separate strings for each heading within the PCRSheet
        if (currStrings == null) {
            current_sample_num = 0;
            usedReagents = new HashSet<>();
            samples = new StringBuilder();
            samples.append("samples: \nlabel\tprimer1\tprimer2\ttemplate\tproduct\n");
            source = new StringBuilder();
            source.append("source: \nname\tlocation\tnote\n");
            destination = new StringBuilder();
            destination.append("destination: thermocycler" + "1A_placeholder\n");
            notes1 = new StringBuilder();
            dilutionDests = new StringBuilder();
        } else {
            samples = new StringBuilder(currStrings.get(0));
            source = new StringBuilder(currStrings.get(1));
            destination = new StringBuilder(currStrings.get(2));
            notes1 = new StringBuilder(currStrings.get(3));
            dilutionDests = new StringBuilder(currStrings.get(4));
        }
        current_sample_num++;
        //casting step into PCR step object
        PCR pcrstep = (PCR) step;
        //extracting info needed for samples
        String label = thread + current_sample_num;
        String oligo1 = pcrstep.getOligo1();
        String oligo2 = pcrstep.getOligo2();
        String template = pcrstep.getTemplate();
        String product = String.format("%s/%s", header, pcrstep.getProduct());
        
        //putting all the above info into samples stringbuilder
        samples.append(label).append("\t").append(oligo1).append("\t")
                .append(oligo2).append("\t").append(template).append('\t').append(product).append("\n");

        //for .get(0) we're assuming that inventory.get() return a Pair<string,string> where
        //the key is the location and the value would be the dilution note if needed.
        if (!usedReagents.contains(template)) {
            usedReagents.add(template);
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
        //will have to check if these are empty to see if dilution is necessary
        String oligo1Note = inventory.get(oligo1, 10.0).getValue();
        String templateNote = inventory.get(template, -1.0).getValue();
        String oligo2Note = inventory.get(oligo2, 10.0).getValue();
        //if there is a dilution necessary, we must include a dilution note, and figure out where the
        //diluted version of the product will go in inventory, as returned by the put() method
        if (!templateNote.equals("")) {
            notes1.append(templateNote).append('\n');
            //an inventory method that returns the next empty slot in the thread/box of interest
            String templateLoc = inventory.put(template, -1.0, "box" + thread);
            dilutionDests.append(template + '\t' + templateLoc);
        }
        if (!oligo1Note.equals("")) {
            notes1.append(oligo1Note).append('\n');
            String oligo1Loc = inventory.put(oligo1, 10.0, "box" + thread);
            dilutionDests.append(oligo1 + '\t' + oligo1Loc);
        }
        if (!oligo2Note.equals("")) {
            notes1.append(oligo2Note).append('\n');
            String oligo2Loc = inventory.put(oligo2, 10.0, "box" + thread);
            dilutionDests.append(oligo2 + '\t' + oligo2Loc);
        }

        System.out.println("PCR:");
        System.out.println(samples.toString());
        List<String> newCurrStrings = new ArrayList<String>(Arrays.asList(samples.toString(), source.toString(), destination.toString(), notes1.toString(), dilutionDests.toString()));
        return newCurrStrings;
    }
}
