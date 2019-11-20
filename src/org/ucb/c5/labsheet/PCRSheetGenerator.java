package org.ucb.c5.labsheet;

import org.ucb.c5.constructionfile.model.PCR;
import org.ucb.c5.constructionfile.model.Step;

import java.util.*;

public class PCRSheetGenerator { //, Inventory
    public void initiate() throws Exception {
    }

    public static String run(List<Step> steps, Inventory inventory, String thread) throws Exception {
        int current_sample_num = 0;
        
        StringBuilder samples = new StringBuilder();
        samples.append("samples: \n label\tprimer1\tprimer2\ttemplate\tproduct\n");
        StringBuilder source = new StringBuilder();
        source.append("source: \n  name\tlocation\tnote\n");
        StringBuilder destination = new StringBuilder();
        destination.append("destination:");

        StringBuilder notes1 = new StringBuilder();
        StringBuilder dilutionDests = new StringBuilder();

        //to ensure we are not creating multiple sources for the same reagent
        HashSet<String> usedReagents = new HashSet<>();
        
        for (Step step: steps) {
            current_sample_num++;
            String label = thread + Integer.toString(current_sample_num);
            PCR pcrstep = (PCR) step;
            String oligo1 = pcrstep.getOligo1();
            usedReagents.add(oligo1);
            String oligo2 =  pcrstep.getOligo2();
            usedReagents.add(oligo2);
            String template = pcrstep.getTemplate();
            usedReagents.add(template);
            String product = pcrstep.getProduct();
            
            //creating separate strings for each heading within the PCRSheet
            samples.append(label).append("\t").append(oligo1).append("\t")
                    .append(oligo2).append("\t").append(template).append('\t').append(product).append("\n");

            //for .get(0) we're assuming that inventory.get() return a Pair<string,string> where
                //the first string is the location and the second string would be the dilution note if needed.
            if (!usedReagents.contains(template)) {
                source.append(template + '\t' + inventory.get(template).get(0), +'\t' +
                        inventory.get(template).get(1) + '\n');
            }
            if (!usedReagents.contains(oligo1)) {
                source.append(oligo1 + '\t' + inventory.get(oligo1).get(0), +'\t' +
                        inventory.get(oligo1).get(1) + '\n');
            }
            if (!usedReagents.contains(oligo2)) {
                source.append(oligo2 + '\t' + inventory.get(oligo2).get(0), +'\t' +
                        inventory.get(oligo2).get(1) + '\n');
            }
            destination.append("thermocycler" + "1b_placeholder\n");
            //will have to check if these are null to see if dilution is necessary
            String templateNote = inventory.get(template).get(1);
            String oligo1Note = inventory.get(oligo1).get(1);
            String oligo2Note = inventory.get(oligo2).get(1);
            if (templateNote != null) {
                notes1.append(templateNote + '\n');
                //an inventory method that returns the next empty slot in the thread/box of interest
                //Location templateLoc = inventory.getNext();
                dilutionDests.append(template + '\t' + templateLoc);
                inventory.put(template, templateLoc);
            }
            if (oligo1Note != null) {
                notes1.append(oligo1Note + '\n');
                //Location oligo1Loc = inventory.getNext();
                dilutionDests.append(oligo1 + '\t' + oligo1Loc);
                inventory.put(oligo1, oligo1Loc);
            }
            if (oligo2Note != null) {
                notes1.append(oligo2Note + '\n');
                //Location oligo2Loc = inventory.getNext();
                dilutionDests.append(oligo2 + '\t' + oligo2Loc);
                inventory.put(oligo1, oligo2Loc);
            }

        }
        String PCRSheet = thread + " : PCR\n\n" + samples.toString() + '\n' + source.toString() + '\n' +
                destination.toString() + '\n' + notes1.toString() + '\n' + dilutionDests.toString() + '\n';
        return PCRSheet;
    }


}
