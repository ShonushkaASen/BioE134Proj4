package org.ucb.c5.labsheet;

import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.constructionfile.model.Transformation;
//import org.ucb.c5.inventory.Inventory;

import java.util.List;

public class TransformSheetGenerator {
//contains transform, plate, pick, miniprep (i.e. those are called within this class)
    public void initiate() throws Exception {

    }

    public static String run(List<Step> steps, Inventory inventory, String thread) throws Exception {
        String source = "source:" + "thermocycler_placeholder\n";
        StringBuilder samples = new StringBuilder();
        samples.append("samples: \nlabel\tproduct\tstrain\tantibiotic\tincubate\n");
        String rescue_required = "rescue_required: ";

        int current_sample_num = 1;
        for (Step step : steps) {
            String label = thread + current_sample_num;
            Transformation transform = (Transformation) step;
            String product = String.format("oligo2-oligo1/%s", transform.getProduct()); //need to get oligo1 and oligo2
            String strain = transform.getStrain();
            String antibiotic = transform.getAntibiotic().toString();

            samples.append(label).append("\t").append(product).append("\t").append(strain).append("\t")
                    .append(antibiotic).append('\t').append("incubate_placeholder").append("\n");

            if (antibiotic.equals("Amp")) {
                rescue_required += "no\n";
            } else {
                rescue_required += "yes\n";
            }
            current_sample_num++;
        }

        return source + "\n" + samples.toString() + "\n" + rescue_required;
    }

}
