package org.ucb.c5.labsheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.ucb.c5.Inventory.Inventory;
import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.constructionfile.model.Cleanup;

public class PCRCleanupSheetGenerator {
    private static int current_sample_num;
    private static StringBuilder samples;
    private static StringBuilder source;
    public void initiate() throws Exception {

    }
    
    public static List<String> run(Step step, Inventory inventory, String thread, List<String> currStrings) throws Exception {
        if (currStrings == null) {
            current_sample_num = 0;
            source = new StringBuilder();
            source.append("destination: thermocycler" + "1A_placeholder\n");
            samples = new StringBuilder();
            samples.append("samples: \nreaction\tlabel\telution_volume\tdestination\tproduct\n");
          
        } else {
            source = new StringBuilder(currStrings.get(0));
            samples = new StringBuilder(currStrings.get(1));
        }
        current_sample_num++;
        String reaction = thread + current_sample_num;
        String label = reaction + "p";
        Cleanup cleanup = (Cleanup) step;
        String elution_volume = "50µL";
        //tricky part: add the product of the cleanup to the inventory, labeled in such a way that it's distinguishable
        //from other similar PCR operations, so that it may be found for later steps (like digestion or transformation)
        String product = "";
        String destination = inventory.put(product, -1.0, "box" + thread);
     

        //to get locations, the put() method of inventory should return the location
        samples.append(reaction).append("\t").append(label).append("\t").append(elution_volume).append("\t").append(destination).append("\n");
        System.out.println("PCRCleanup:");
        System.out.println(samples.toString());
        List<String> newCurrStrings = new ArrayList<String>(Arrays.asList(source.toString(), samples.toString()));
        return newCurrStrings;
    }
}
