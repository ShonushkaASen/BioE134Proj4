package org.ucb.c5.labsheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ucb.c5.Inventory.Inventory;
import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.constructionfile.model.Transformation;

public class PlateSheetGenerator {
    private static int current_sample_num;
    private static StringBuilder samples;
    
    public void initiate() throws Exception {

    }
    
    public static List<String> run(Step step, Inventory inventory, String thread, List<String> currStrings, String header) throws Exception {
        if (currStrings == null) {
            current_sample_num = 0;
            samples = new StringBuilder();
            samples.append("samples: \n label\tproduct\tstrain\tantibiotic\tincubate\n");
        } else {
            samples = new StringBuilder(currStrings.get(0));
        }
        current_sample_num++;
        
        String label = thread + Integer.toString(current_sample_num);
        Transformation transform = (Transformation) step;
        String product = transform.getProduct();
        String strain = transform.getStrain();
        String antibiotic = transform.getAntibiotic().toString();

        samples.append(label).append("\t").append(product).append("\t").append(strain).append("\t")
                .append(antibiotic).append('\t').append("37C").append("\n");
       
        System.out.println("Plate:");
        System.out.println(samples.toString());
        
        List<String> newCurrStrings = new ArrayList<String>(Arrays.asList(samples.toString()));
        return newCurrStrings;
    }
}
