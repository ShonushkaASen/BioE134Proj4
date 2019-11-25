package org.ucb.c5.labsheet;

import java.util.List;

import org.ucb.c5.Inventory.Inventory;
import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.constructionfile.model.Transformation;

public class PlateSheetGenerator {
    
    public void initiate() throws Exception {

    }
    
    public static String run(List<Step> steps, Inventory inventory, String thread) throws Exception {
        StringBuilder samples = new StringBuilder();
        samples.append("samples: \n label\tproduct\tstrain\tantibiotic\tincubate\n");

        int current_sample_num = 1;
        for (Step step : steps) {
            String label = thread + Integer.toString(current_sample_num);
            Transformation transform = (Transformation) step;
            String product = transform.getProduct();
            String strain = transform.getStrain();
            String antibiotic = transform.getAntibiotic().toString();
            
            samples.append(label).append("\t").append(product).append("\t").append(strain).append("\t")
                    .append(antibiotic).append('\t').append("incubate_placeholder").append("\n");
            current_sample_num++;
        }
        return samples.toString();
    }
}
