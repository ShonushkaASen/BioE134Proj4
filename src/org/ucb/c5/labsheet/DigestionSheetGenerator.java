package org.ucb.c5.labsheet;

import java.util.ArrayList;
import java.util.Arrays;
import org.ucb.c5.constructionfile.model.Digestion;
import org.ucb.c5.constructionfile.model.Step;

import java.util.List;
import org.ucb.c5.Inventory.Inventory;
/**
 * @author Shonushka Sen
 */

public class DigestionSheetGenerator {
    private static int current_sample_num;// for labeling purposes
    private static StringBuilder samples;
    private static StringBuilder destination;
    private static StringBuilder notes;

    public void initiate() throws Exception {

    }


    public static List<String> run(Step step,  Inventory inventory, String thread, List<String> currStrings, String header) throws Exception {
        //if currStrings is null, this means this is the first time a Digestion step has been encountered
        if (currStrings == null) {
            current_sample_num = 0;
            samples = new StringBuilder();
            samples.append(thread).append(": Digest \n\n").append("samples: \nlabel\tdna\tsource\tproduct\n");
            destination = new StringBuilder();
            destination.append("destination: thermocycler_placeholder\n");
            notes = new StringBuilder();
          
        } else {
            //since we know the order in which we are returning the strings, we can reassign the stringbuilders accordingly
            samples = new StringBuilder(currStrings.get(0));
            destination = new StringBuilder(currStrings.get(1));
            notes = new StringBuilder(currStrings.get(2));
        }
        current_sample_num++;
        //sample label should correspond to thread + sample#, i.e. A1
        String label = thread + current_sample_num;
        Digestion digestion = (Digestion) step;
        String product = header + "/" + digestion.getProduct();
        //to find the location of the reactant, we ask inventory where the header+substrate is, i.e. pTarg1/pcrprdt
        String substrate = header + "/" + digestion.getSubstrate();
        String source = inventory.get(substrate, -1.0).getKey();
        //appending information to samples in appropriate order to correspond with heading
        samples.append(label + "\t").append(label + "p\t").append(source + "\t").append(product + "\n");
        
        //converting Stringbuilders to String and passing them into an array to be returned
        List<String> newCurrStrings = new ArrayList<String>(Arrays.asList(samples.toString(), destination.toString(), notes.toString()));
        return newCurrStrings;
    }
}
