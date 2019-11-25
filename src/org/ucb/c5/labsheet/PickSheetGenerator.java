package org.ucb.c5.labsheet;

import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.constructionfile.model.Transformation;

import java.util.List;
import org.ucb.c5.Inventory.Inventory;

public class PickSheetGenerator {

    public void initiate() throws Exception {

    }

    public static String run(List<Step> steps, Inventory inventory, String thread) throws Exception {
        StringBuilder samples = new StringBuilder();
        samples.append("samples: \n source\tproduct\tstrain\tantibiotic\tincubate\tnumber\tlabels\n");

        int current_sample_num = 1;
        for (Step step : steps) {
            String source = thread + current_sample_num;
            Transformation transform = (Transformation) step;
            String product = transform.getProduct();
            String strain = transform.getStrain();
            String antibiotic = transform.getAntibiotic().toString();

            String label1 = thread + Integer.toString(current_sample_num) + "A";
            String label2 = thread + Integer.toString(current_sample_num) + "B";



            samples.append(source).append("\t").append(product).append("\t").append(strain).append("\t")
                    .append(antibiotic).append('\t').append("incubate_placeholder").append("2").append(label1)
                    .append(", ").append(label2).append("\n");
            current_sample_num++;
        }
        return samples.toString();
    }
}
