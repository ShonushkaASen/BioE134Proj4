package org.ucb.c5.labsheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ucb.c5.Inventory.Inventory;
import org.ucb.c5.constructionfile.model.Ligation;
import org.ucb.c5.constructionfile.model.Step;

public class LigationSheetGenerator {
    private static int current_sample_num;
    private static StringBuilder samples;
    private static StringBuilder reaction;
    private static StringBuilder destination;
    
    public void initiate() throws Exception {

    }
    
    public static List<String> run(Step step, Inventory inventory, String thread, List<String> currStrings) throws Exception {
        if (currStrings == null) {
            current_sample_num = 0;
            samples = new StringBuilder();
            reaction = new StringBuilder();
            destination = new StringBuilder();
            samples.append("samples: \n label\tdigest\tsource\tproduct\n");
            reaction.append("reaction: \n7.5µL ddH2O\n1µL T4 DNA Ligase Buffer\n1µL DNA\n0.5µL T4 DNA Ligase\n");
            destination.append("destination: thermocycler_placeholder\n");
          
        } else {
            reaction = new StringBuilder(currStrings.get(0));
            samples = new StringBuilder(currStrings.get(1));
            destination = new StringBuilder(currStrings.get(2));
        }
        current_sample_num++;
        
        
        String label = thread + Integer.toString(current_sample_num);
        String digest = label + "d";
        Ligation ligation = (Ligation) step;
        //String product = ligation.getProduct() + "/lig";
        String product = "";
        String source = inventory.get(product, -1.0).getKey();

        //to get locations, the put() method of inventory should return the location
        samples.append(label).append("\t").append(digest).append("\t").append(source).append("\t").append(product).append("\n");
        
        System.out.println("Ligation:");
        System.out.println(samples.toString());
        
        List<String> newCurrStrings = new ArrayList<String>(Arrays.asList(reaction.toString(), samples.toString(), destination.toString()));
        return newCurrStrings;
    }
}
