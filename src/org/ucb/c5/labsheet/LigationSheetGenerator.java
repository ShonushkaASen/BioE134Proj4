package org.ucb.c5.labsheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ucb.c5.Inventory.Inventory;
import org.ucb.c5.constructionfile.model.Ligation;
import org.ucb.c5.constructionfile.model.Step;

public class LigationSheetGenerator {
    private static int current_sample_num; //for labeling purposes
    private static StringBuilder samples;
    private static StringBuilder reaction;
    private static StringBuilder destination;
    private static StringBuilder notes;
    
    public void initiate() throws Exception {

    }
    
    public static List<String> run(Step step, Inventory inventory, String thread, List<String> currStrings, String header) throws Exception {
        //if currStrings is null, this means this is the first time a Digestion step has been encountered
        if (currStrings == null) {
            current_sample_num = 0;
            samples = new StringBuilder();
            reaction = new StringBuilder();
            destination = new StringBuilder();
            notes = new StringBuilder();
            samples.append("samples: \nlabel\tdigest\tsource\tproduct\n");
            reaction.append(thread).append(": Ligation \n\n").append("reaction: \n7.5µL ddH2O\n1µL T4 DNA Ligase Buffer\n1µL DNA\n0.5µL T4 DNA Ligase\n");
            destination.append("destination: thermocycler_placeholder\n");
            notes.append("Note:\nNever let enzymes warm up!  Only take the enzyme cooler out of the freezer when you are actively using it, and only take the tubes out of it when actively dispensing.\n");          
        } else {
            //since we know the order in which we are returning the strings, we can reassign the stringbuilders accordingly
            reaction = new StringBuilder(currStrings.get(0));
            samples = new StringBuilder(currStrings.get(1));
            destination = new StringBuilder(currStrings.get(2));
            notes = new StringBuilder(currStrings.get(3));
        }
        current_sample_num++;
        
        //sample label should correspond to thread + sample#, i.e. A1
        String label = thread + Integer.toString(current_sample_num);
        String digest = label + "d";
        Ligation ligation = (Ligation) step;
        String product = header + "/lig";
        //to find the location of the reactant, we ask inventory where the header+fragment is, i.e. pTarg1/pcrA
        String fragment = header + "/" + ligation.getFragments().get(0);
        String source = inventory.get(fragment, -1.0).getKey();
        //appending information to samples in appropriate order to correspond with heading
        samples.append(label).append("\t").append(digest).append("\t").append(source).append("\t").append(product).append("\n");
        
        //converting Stringbuilders to String and passing them into an array to be returned
        List<String> newCurrStrings = new ArrayList<String>(Arrays.asList(reaction.toString(), samples.toString(), destination.toString(), notes.toString()));
        return newCurrStrings;
    }
}
