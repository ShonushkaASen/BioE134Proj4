package org.ucb.c5.labsheet;

import java.util.ArrayList;
import java.util.Arrays;
import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.constructionfile.model.Transformation;

import java.util.List;
import org.ucb.c5.Inventory.Inventory;

public class PickSheetGenerator {
    private static int current_sample_num;
    private static StringBuilder samples;
    
    public void initiate() throws Exception {

    }

    public static List<String> run(Step step, Inventory inventory, String thread, List<String> currStrings, String header) throws Exception {
         if (currStrings == null) {
            current_sample_num = 0;
            samples = new StringBuilder();
            samples.append(thread).append(": Pick \n\n").append("samples: \nsource\tproduct\tstrain\tantibiotic\tincubate\tnum\tlabels\n");
          
        } else {
            samples = new StringBuilder(currStrings.get(0));
        }
        current_sample_num++;
 
        String source = thread + current_sample_num;
        Transformation transform = (Transformation) step;
        String product = transform.getProduct();
        String strain = transform.getStrain();
        String antibiotic = transform.getAntibiotic().toString();

        String label1 = thread + Integer.toString(current_sample_num) + "A";
        String label2 = thread + Integer.toString(current_sample_num) + "B";



        samples.append(source).append("\t").append(product).append("\t").append(strain).append("\t")
                .append(antibiotic).append('\t').append("37C").append("\t2").append("\t").append(label1)
                .append(", ").append(label2).append("\n");
        
        System.out.println("Pick:");
        System.out.println(samples.toString());
   
        List<String> newCurrStrings = new ArrayList<String>(Arrays.asList(samples.toString()));
        return newCurrStrings;
    }
}
