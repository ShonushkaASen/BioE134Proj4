package org.ucb.c5.labsheet;

import org.ucb.c5.constructionfile.model.Antibiotic;
import org.ucb.c5.constructionfile.model.Operation;
import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.constructionfile.model.Transformation;

import java.util.List;

public class TransformSheetGenerator {
//contains transform, plate, pick, miniprep (i.e. those are called within this class)
    public void initiate() throws Exception {

    }

    public static String run(List<Step> steps, Inventory inventory, String thread) throws Exception {
        int current_sample_num = 0;
        StringBuilder source = new StringBuilder();
        StringBuilder samples = new StringBuilder();
        source.append("source: thermocycler_placeholder\n");
        samples.append("samples: \n label\tproduct\tstrain\tantibiotic\tincubate\n");
        String rescue_required = "rescue_required: ";

        for (Step step : steps) {
            current_sample_num++;
            String label = thread + Integer.toString(current_sample_num);
            Transformation transform = (Transformation) step;
            String product = transform.getProduct();
            String strain = transform.getStrain();
            String antibiotic = transform.getAntibiotic().toString();
//            Operation operation = transform.getOperation();

            samples.append(label).append("\t").append(product).append("\t").append(strain).append("\t")
                    .append(antibiotic).append('\t').append("incubate_placeholder").append("\n");

            if (antibiotic.equals("Amp")) {
                rescue_required += "no\n";
            } else {
                rescue_required += "yes\n";
            }
        }


        return source.toString() + "\n" + samples.toString() + "\n" + rescue_required;
    }

}
