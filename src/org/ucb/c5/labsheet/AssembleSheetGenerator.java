package org.ucb.c5.labsheet;

import java.util.ArrayList;
import java.util.Arrays;

import org.ucb.c5.constructionfile.model.Assembly;
import org.ucb.c5.constructionfile.model.Digestion;
import org.ucb.c5.constructionfile.model.Step;

import java.util.List;
import org.ucb.c5.Inventory.Inventory;

public class AssembleSheetGenerator {
    private static int current_sample_num;
    private static StringBuilder source;
    private static StringBuilder samples;
    private static StringBuilder destination;
    private static StringBuilder fragments;

    public void initiate() throws Exception {

    }

    public static List<String> run(Step step,  Inventory inventory, String thread, List<String> currStrings, String header) throws Exception {
        if (currStrings == null) {
            current_sample_num = 0;
            source = new StringBuilder();
            source.append(thread + ": Assemble\n" + "source \n dna\tlocation\n");
            destination = new StringBuilder();
            destination.append("destination:");
            samples = new StringBuilder();
            samples.append("samples \n label\tfragments\tproduct\n");
            fragments = new StringBuilder();
          
        } else {
            source = new StringBuilder(currStrings.get(0));
            samples = new StringBuilder(currStrings.get(1));
            destination = new StringBuilder(currStrings.get(2));
        }
        Assembly assembly = (Assembly) step;
        
        current_sample_num++;
        String dna1 = thread + current_sample_num + "p";
        current_sample_num++;
        String dna2 = thread + current_sample_num + "p";
        String label = thread;
        String product = assembly.getProduct();

        fragments.append(dna1).append(",").append(dna2);
        String fragString = fragments.toString();
        
        String substrate1 = header + "/" + assembly.getFragments().get(0);
        String substrate2 = header + "/" + assembly.getFragments().get(1);
        String location1 = inventory.get(substrate1, -1.0).getKey();
        String location2 = inventory.get(substrate2, -1.0).getKey();
        
        
        source.append(dna1 + "\t").append(location1).append("\n").append(dna2 + "\t").append(location2 + "\n");
        samples.append(label + "\t").append(fragString + "\t").append(product + "\n");
                
        
        System.out.println("Assembly:");
        System.out.println(samples.toString());
        List<String> newCurrStrings = new ArrayList<String>(Arrays.asList(source.toString(), samples.toString(), destination.toString()));
        return newCurrStrings;
    }
}