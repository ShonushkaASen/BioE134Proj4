package org.ucb.c5.labsheet;

import org.ucb.c5.constructionfile.model.Antibiotic;
import org.ucb.c5.constructionfile.model.Operation;
import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.constructionfile.model.Transformation;

import java.util.List;

public class TransformSheetGenerator {
    // parts on sheet: source, samples, rescue_required

    public void initiate() throws Exception {

    }

    public static String run(List<Step> steps, Inventory inventory) throws Exception {
        StringBuilder source = new StringBuilder();
        StringBuilder samples = new StringBuilder();
        samples.append("samples: \n label\tproduct\tstrain\tantibiotic\tincubate\n");
        String rescue_required = "rescue_required: ";

        for (Step step : steps) {
            Transformation transform = (Transformation) step;
            String product = transform.getProduct();
            String strain = transform.getStrain();
            String antibiotic = transform.getAntibiotic().toString();
//            Operation operation = transform.getOperation();

            samples.append("label_placeholder\t").append(product).append("\t").append(strain).append("\t")
                    .append(antibiotic).append('\t').append("incubate_placeholder").append("\n");

        }


        return source.toString() + "\n" + samples.toString() + "\n" + rescue_required;
    }

}
