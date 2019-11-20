package org.ucb.c5.labsheet;

import java.util.List;
import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.constructionfile.model.Transformation;

public class PlateSheetGenerator {
    
    public void initiate() throws Exception {

    }
    
    public static String run(List<Step> steps, Inventory inventory, String thread) throws Exception {
        int current_sample_num = 0;
        StringBuilder samples = new StringBuilder();
        samples.append("samples: \n label\tproduct\tstrain\tantibiotic\tincubate\n");
        for (Step step : steps) {
            current_sample_num++;
            String label = thread + Integer.toString(current_sample_num);
            Transformation transform = (Transformation) step;
            String product = transform.getProduct();
            String strain = transform.getStrain();
            String antibiotic = transform.getAntibiotic().toString();
            
            samples.append(label).append("\t").append(product).append("\t").append(strain).append("\t")
                    .append(antibiotic).append('\t').append("incubate_placeholder").append("\n");
        }
        return samples.toString();
    }
}
