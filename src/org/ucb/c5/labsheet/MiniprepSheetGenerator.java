package org.ucb.c5.labsheet;

import org.ucb.c5.inventory.Inventory;
import java.util.List;
import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.constructionfile.model.Transformation;

public class MiniprepSheetGenerator {
    public void initiate() throws Exception {

    }
    
    public static String run(List<Step> steps, Inventory inventory, String thread) throws Exception {
        StringBuilder samples = new StringBuilder();
        samples.append("samples: \n culture\tlabel\tlocation\n");

        int current_sample_num = 1;
        for (Step step : steps) {
            String culture1 = thread + current_sample_num + "A";
            String culture2 = thread + current_sample_num + "B";
            Transformation transform = (Transformation) step;
            String label1 = transform.getProduct() + "-A";
            String label2 = transform.getProduct() + "-B";

            
            samples.append(culture1).append("\t").append(label1).append("\t").append("location_placeholder").append("\n");
            samples.append(culture2).append("\t").append(label2).append("\t").append("location_placeholder").append("\n");
            current_sample_num++;
        }
        return samples.toString();
    }
}
