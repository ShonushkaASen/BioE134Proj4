package org.ucb.c5.labsheet;

import Inventory;
import java.util.List;
import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.constructionfile.model.Transformation;

public class MiniprepSheetGenerator {
    public void initiate() throws Exception {

    }
    
    public static String run(List<Step> steps, Inventory inventory, String thread) throws Exception {
        int current_sample_num = 0;
        StringBuilder samples = new StringBuilder();
        samples.append("samples: \n culture\tlabel\tlocation\n");
        for (Step step : steps) {
            current_sample_num++;
            String culture1 = thread + Integer.toString(current_sample_num) + "A";
            String culture2 = thread + Integer.toString(current_sample_num) + "B";
            Transformation transform = (Transformation) step;
            String label1 = transform.getProduct() + "-A";
            String label2 = transform.getProduct() + "-B";

            //to get locations, the put() method of inventory should return the location
            samples.append(culture1).append("\t").append(label1).append("\t").append("location_placeholder").append("\n");
            samples.append(culture2).append("\t").append(label2).append("\t").append("location_placeholder").append("\n");
        }
        return samples.toString();
    }
}
