package org.ucb.c5.labsheet;

import java.util.ArrayList;
import java.util.Arrays;
import org.ucb.c5.Inventory.Inventory;
import java.util.List;
import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.constructionfile.model.Transformation;

public class MiniprepSheetGenerator {
    private static int current_sample_num;
    private static StringBuilder samples;
    
    public void initiate() throws Exception {

    }
    
    public static List<String> run(Step step, Inventory inventory, String thread, List<String> currStrings, String header) throws Exception {
        if (currStrings == null) {
            current_sample_num = 0;
            samples = new StringBuilder();
            samples.append("samples: \n culture\tlabel\tlocation\n");
          
        } else {
            samples = new StringBuilder(currStrings.get(0));
        }
        current_sample_num++;
        
        String culture1 = thread + current_sample_num + "A";
        String culture2 = thread + current_sample_num + "B";
        Transformation transform = (Transformation) step;
        String label1 = transform.getProduct() + "-A";
        String label2 = transform.getProduct() + "-B";
        
        String location1 = inventory.put(label1, -1.0, "box" + thread);
        String location2 = inventory.put(label2, -1.0, "box" + thread);

        //to get locations, the put() method of inventory should return the location
        samples.append(culture1).append("\t").append(label1).append("\t").append(location1).append("\n");
        samples.append(culture2).append("\t").append(label2).append("\t").append(location2).append("\n");
       
        System.out.println("Miniprep:");
        System.out.println(samples.toString());
        
        List<String> newCurrStrings = new ArrayList<String>(Arrays.asList(samples.toString()));
        return newCurrStrings;
    }
}
