package org.ucb.c5.labsheet;

import org.ucb.c5.constructionfile.model.Assembly;
import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.constructionfile.model.Transformation;
import org.ucb.c5.constructionfile.model.Inventory;

import java.util.List;

public class AssembleSheetGenerator {

    public void initiate() throws Exception {

    }

    public static String run(List<Step> steps, Inventory inventory, String thread) throws Exception {
        
        StringBuilder source = new StringBuilder();
        source.append("source \n dna\tlocation\n");

        int current_sample_num = 1;

        StringBuilder fragments = new StringBuilder();
        StringBuilder product = new StringBuilder();

        for (Step step: steps) {
            String dna = thread + current_sample_num + "p";
            String location = "box" + thread + "\B" + Integer.toString(current_sample_num);
            source.append(dna).append("\t").append(location).append("\n").append("\n");
            fragments.append(dna + ",");

            Assembly assembly = (Assembly) step;

            if (current_sample_num == 1) {
                product.append(assembly.getProduct());
            }

            current_sample_num++;

        }

        StringBuilder samples = new StringBuilder();
        samples.append("samples \n label\tfragments\tproduct\n"):
        
        String label = thread + Integer.toString(current_sample_num);
        String fragmentString = fragments.toString();
        int fragLength = fragmentString.length();
        String finalFragmentString = fragmentString.substring(0, fragLength - 1);
        
        samples.append(label).append("\t").append(finalFragmentString).append("\t")
            .append(product.toString()).append("\n");
        
        source.append(samples.toString());

        
        return source.toString();
    }
}
