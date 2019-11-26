package org.ucb.c5.labsheet;

import java.util.ArrayList;
import java.util.Arrays;
import org.ucb.c5.Inventory.Inventory;
import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.constructionfile.model.Transformation;

import java.util.List;

public class TransformSheetGenerator {
//contains transform, plate, pick, miniprep (i.e. those are called after this class)
    private static int current_sample_num;
    private static StringBuilder samples;
    private static String source;
    private static String rescue_required;
    
    public void initiate() throws Exception {

    }

    public static List<String> run(Step step, Inventory inventory, String thread, List<String> currStrings, String header) throws Exception {
        Transformation transform = (Transformation) step;
        String antibiotic = transform.getAntibiotic().toString();

        if (currStrings == null) {
            current_sample_num = 0;
            samples = new StringBuilder();
            samples.append("samples: \nlabel\tproduct\tstrain\tantibiotic\tincubate\n");
            source = thread + ": Transform \n\n" + "source:" + "thermocycler_placeholder\n";
            
            rescue_required = "rescue_required: ";
            
            if (antibiotic.equals("Amp")) {
                rescue_required += "no\n";
            } else {
                rescue_required += "yes\n";
            }
        } else {
            source = currStrings.get(0);
            samples = new StringBuilder(currStrings.get(1));
            rescue_required = currStrings.get(2);
        }
        current_sample_num++;
        
        

        String label = thread + current_sample_num;
        String product = transform.getProduct(); //need to get oligo1 and oligo2
        String strain = transform.getStrain();

        samples.append(label).append("\t").append(product).append("\t").append(strain).append("\t")
                .append(antibiotic).append('\t').append("37C").append("\n");

        System.out.println("Transformation:");
        System.out.println(samples.toString());
        
        List<String> newCurrStrings = new ArrayList<String>(Arrays.asList(source.toString(), samples.toString(), rescue_required));
        return newCurrStrings;
    }
}
