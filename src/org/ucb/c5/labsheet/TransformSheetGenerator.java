package org.ucb.c5.labsheet;

import java.util.ArrayList;
import java.util.Arrays;
import org.ucb.c5.Inventory.Inventory;
import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.constructionfile.model.Transformation;

import java.util.List;

/**
 * @author Katlyn Ho
 */

public class TransformSheetGenerator {
//always followed by plate, pick, miniprep (i.e. those generators are called after this class)
    private static int current_sample_num; //for labeling purposes
    private static StringBuilder samples;
    private static String source;
    private static String rescue_required;
    private static String notes;
    
    public void initiate() throws Exception {

    }

    public static List<String> run(Step step, Inventory inventory, String thread, List<String> currStrings, String header) throws Exception {
        Transformation transform = (Transformation) step;
        String antibiotic = transform.getAntibiotic().toString();
        //if currStrings is null, this means this is the first time a Miniprep step has been encountered
        if (currStrings == null) {
            current_sample_num = 0;
            samples = new StringBuilder();
            samples.append("samples: \nlabel\tproduct\tstrain\tantibiotic\tincubate\n");
            source = thread + ": Transform \n\n" + "source:" + "thermocycler_placeholder\n";
            
            rescue_required = "rescue_required: ";
            //rescue is required for all antibiotics except ampicillin
            if (antibiotic.equals("Amp")) {
                rescue_required += "no\n";
                notes = "";
            } else {
                rescue_required += "yes\n";
                notes = String.format("Because the antibiotic is %s, there needs to be a recovery step. Transfer your cells to an eppendorf tube with recovery media/antibiotic and place in shaker.\n", antibiotic);
            }
        } else {
            //since we know the order in which we are returning the strings, we can reassign the stringbuilders accordingly
            source = currStrings.get(0);
            samples = new StringBuilder(currStrings.get(1));
            rescue_required = currStrings.get(2);
            notes = currStrings.get(3);
        }
        current_sample_num++;
        
        
        //label should be thread + sample#, i.e. A1, A2
        String label = thread + current_sample_num;
        String product = transform.getProduct(); 
        String strain = transform.getStrain();
        //appending information to samples in appropriate order to correspond with heading
        samples.append(label).append("\t").append(product).append("\t").append(strain).append("\t")
                .append(antibiotic).append('\t').append("37C").append("\n");

        //converting Stringbuilders to String and passing them into an array to be returned
        List<String> newCurrStrings = new ArrayList<String>(Arrays.asList(source.toString(), samples.toString(), rescue_required, notes));
        return newCurrStrings;
    }
}
