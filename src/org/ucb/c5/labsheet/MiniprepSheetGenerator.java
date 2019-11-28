package org.ucb.c5.labsheet;

import java.util.ArrayList;
import java.util.Arrays;
import org.ucb.c5.Inventory.Inventory;
import java.util.List;
import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.constructionfile.model.Transformation;
/**
 * @author Heloise Carion
 **/

public class MiniprepSheetGenerator {
    private static int current_sample_num;// for labeling purposes
    private static StringBuilder samples;
    private static String note;
    
    public void initiate() throws Exception {

    }
    
    public static List<String> run(Step step, Inventory inventory, String thread, List<String> currStrings, String header) throws Exception {
        //if currStrings is null, this means this is the first time a Miniprep step has been encountered
        if (currStrings == null) {
            current_sample_num = 0;
            samples = new StringBuilder();
            samples.append(thread).append(": Miniprep \n\n").append("samples: \nculture\tlabel\tlocation\n");
            note = "Note:\nWrite the abbreviated sample name on the top of the eppendorf tube, and write the full name of the product on the side.\n";
          
        } else {
            //since we know the order in which we are returning the strings, we can reassign the stringbuilders accordingly
            samples = new StringBuilder(currStrings.get(0));
            note = currStrings.get(1);
        }
        current_sample_num++;
        //for one sample, there will be 2 colonies aka 2 minipreps
        //cultures will correspond to, for example, A1A and A1B
        String culture1 = thread + current_sample_num + "A";
        String culture2 = thread + current_sample_num + "B";
        Transformation transform = (Transformation) step;
        //labels will be, for example, pTarget-cscB1-A and pTarget-cscB1-B
        String label1 = transform.getProduct() + "-A";
        String label2 = transform.getProduct() + "-B";
        //inventory.put() returns the location in inventory in which the sample has been placed
        String location1 = inventory.put(label1, -1.0, "box" + thread).replace("null/","");
        String location2 = inventory.put(label2, -1.0, "box" + thread);
        //appending information to samples in appropriate order to correspond with heading
        samples.append(culture1).append("\t").append(label1).append("\t").append(location1).append("\n");
        samples.append(culture2).append("\t").append(label2).append("\t").append(location2).append("\n");
       
        //converting Stringbuilders to String and passing them into an array to be returned
        List<String> newCurrStrings = new ArrayList<String>(Arrays.asList(samples.toString(), note));
        return newCurrStrings;
    }
}
