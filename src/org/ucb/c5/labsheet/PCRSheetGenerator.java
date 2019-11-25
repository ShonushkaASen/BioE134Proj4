package org.ucb.c5.labsheet;

import org.ucb.c5.Inventory.Inventory;
import org.ucb.c5.constructionfile.model.PCR;
import org.ucb.c5.constructionfile.model.Step;

import java.util.*;

public class PCRSheetGenerator {
    public void initiate() throws Exception {
    }

    public static String run(List<Step> steps, Inventory inventory, String thread) throws Exception {
        StringBuilder samples = new StringBuilder();
        samples.append("samples: \nlabel\tprimer1\tprimer2\ttemplate\tproduct\n");
        StringBuilder source = new StringBuilder();
        source.append("source: \nname\tlocation\tnote\n");
        StringBuilder destination = new StringBuilder();
        destination.append("destination:");

        StringBuilder notes1 = new StringBuilder();
        StringBuilder dilutionDests = new StringBuilder();

        //to ensure we are not creating multiple sources for the same reagent
        HashSet<String> usedReagents = new HashSet<>();

        int current_sample_num = 1;
        for (Step step : steps) {
            String label = thread + current_sample_num;
            PCR pcrstep = (PCR) step;
            String oligo1 = pcrstep.getOligo1();
            usedReagents.add(oligo1);
            String oligo2 = pcrstep.getOligo2();
            usedReagents.add(oligo2);
            String template = pcrstep.getTemplate();
            usedReagents.add(template);
            String product = String.format("%s-%s/%s", template, oligo1, pcrstep.getProduct());

            //creating separate strings for each heading within the PCRSheet
            samples.append(label).append("\t").append(oligo1).append("\t")
                    .append(oligo2).append("\t").append(template).append('\t').append(product).append("\n");

            //for .get(0) we're assuming that inventory.get() return a Pair<string,string> where
            //the first string is the location and the second string would be the dilution note if needed.
            if (!usedReagents.contains(template)) {
                source.append(template + '\t' + inventory.get(template, -1.0).getKey() +'\t' +
                        inventory.get(template, -1.0).getValue() + '\n');
            }
            if (!usedReagents.contains(oligo1)) {
                source.append(oligo1 + '\t' + inventory.get(oligo1, -1.0).getKey() +'\t' +
                        inventory.get(oligo1, -1.0).getValue() + '\n');
            }
            if (!usedReagents.contains(oligo2)) {
                source.append(oligo2 + '\t' + inventory.get(oligo2, -1.0).getKey() +'\t' +
                        inventory.get(oligo2, -1.0).getValue() + '\n');
            }
            destination.append("thermocycler" + "1A_placeholder\n");
            //will have to check if these are null to see if dilution is necessary
            String oligo1Note = inventory.get(oligo1, -1.0).getValue();
            String templateNote = inventory.get(template, -1.0).getValue();
            String oligo2Note = inventory.get(oligo2, -1.0).getValue();
            if (!templateNote.equals("-1")) {
                notes1.append(templateNote).append('\n');
                //an inventory method that returns the next empty slot in the thread/box of interest
                String templateLoc = inventory.put(template, -1.0, "box" + thread);
                dilutionDests.append(template + '\t' + templateLoc);
                inventory.put(template, -1.0, thread);
            }
            if (!oligo1Note.equals("-1")) {
                notes1.append(oligo1Note).append('\n');
                String oligo1Loc = inventory.put(oligo1, -1.0, "box" + thread);
                dilutionDests.append(oligo1 + '\t' + oligo1Loc);
                inventory.put(oligo1, -1.0, thread);
            }
            if (!oligo2Note.equals("-1")) {
                notes1.append(oligo2Note).append('\n');
                String oligo2Loc = inventory.put(oligo2, -1.0, "box" + thread);
                dilutionDests.append(oligo2 + '\t' + oligo2Loc);
                inventory.put(oligo1, -1.0, thread);
            }
            current_sample_num++;
        }
        String PCRSheet = thread + " : PCR\n\n" + samples.toString() + '\n' + source.toString() + '\n' +
                destination.toString() + '\n' + notes1.toString() + '\n' + dilutionDests.toString() + '\n';
        return PCRSheet;
    }
}
