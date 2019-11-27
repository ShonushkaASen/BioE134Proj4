package org.ucb.c5.labsheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.ucb.c5.Inventory.Inventory;
import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.constructionfile.model.Cleanup;

public class PCRCleanupSheetGenerator {
    private static int current_sample_num; //for labeling purposes
    private static StringBuilder samples; //indicates PCR reaction reactants
    private static StringBuilder source; //indicates where user can find reactants
    public void initiate() throws Exception {

    }
    
    public static List<String> run(Step step, Inventory inventory, String thread, List<String> currStrings, String header) throws Exception {
        //if currStrings is null, this means this is the first time a PCRCleanup step has been encountered
        if (currStrings == null) {
            current_sample_num = 0;
            source = new StringBuilder();
            source.append(thread).append(": Zymo \n\n").append("source: thermocycler" + "1A_placeholder\n");
            samples = new StringBuilder();
            samples.append("samples: \nreaction\tlabel\telution_volume\tdestination\tproduct\n");
          
        } else {
            //since we know the order in which we are returning the strings, we can reassign the stringbuilders accordingly
            source = new StringBuilder(currStrings.get(0));
            samples = new StringBuilder(currStrings.get(1));
        }
        //each sample should be labeled "thread + #", i.e. A1, A2
        current_sample_num++;
        String reaction = thread + current_sample_num;
        String label = reaction + "p";
        Cleanup cleanup = (Cleanup) step;
        String elution_volume = "50µL";
        //product name would be for example pTarg2/pcrpdt
        String product = header + "/" + cleanup.getProduct();
        //inventory.put() returns the location in inventory in which the sample has been placed
        String destination = inventory.put(product, -1.0, "box" + thread);
     
        //appending attributes to samples in the appropriate order to match with headings
        samples.append(reaction).append("\t").append(label).append("\t").append(elution_volume).append("\t").append(destination).append("\t").append(product).append("\n");

        //converting Stringbuilders to String and passing them into an array to be returned
        List<String> newCurrStrings = new ArrayList<String>(Arrays.asList(source.toString(), samples.toString()));
        return newCurrStrings;
    }
}
