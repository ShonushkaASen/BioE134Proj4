package org.ucb.c5.labsheet;

import java.util.ArrayList;
import java.util.Arrays;
import org.ucb.c5.constructionfile.model.Digestion;
import org.ucb.c5.constructionfile.model.Step;

import java.util.List;
import org.ucb.c5.Inventory.Inventory;

public class DigestionSheetGenerator {
    private static int current_sample_num;
    private static StringBuilder samples;
    private static StringBuilder destination;

    public void initiate() throws Exception {

    }


    public static List<String> run(Step step,  Inventory inventory, String thread, List<String> currStrings) throws Exception {
        if (currStrings == null) {
            current_sample_num = 0;
            samples = new StringBuilder();
            samples.append("samples: \nlabel\tdna\tsource\tproduct\n");
            destination = new StringBuilder();
            destination.append("destination:");
          
        } else {
            samples = new StringBuilder(currStrings.get(0));
            destination = new StringBuilder(currStrings.get(1));
        }
        current_sample_num++;
        
        String label = thread + current_sample_num;
        Digestion digestion = (Digestion) step;
        //String product = String.format("oligo2-oligo1/%s", digestion.getProduct());
        String product = "";
        //String source = "box" + thread + "/" + thread + Integer.toString(current_sample_num);
        String source = inventory.get(product, -1.0).getKey();
        samples.append(label + "\t").append(label + "p\t").append(source + "\t").append(product + "\n");
        
        System.out.println("Digestion:");
        System.out.println(samples.toString());
        List<String> newCurrStrings = new ArrayList<String>(Arrays.asList(samples.toString(), destination.toString()));
        return newCurrStrings;
    }
}
