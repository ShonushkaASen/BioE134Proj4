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

    public static List<String> run(Step step,  Inventory inventory, String thread, List<String> currStrings) throws Exception {
        if (currStrings == null) {
            current_sample_num = 0;
            source = new StringBuilder();
            source.append("source \n dna\tlocation\n");
            destination = new StringBuilder();
            destination.append("destination:");
            samples  = new StringBuilder();
            samples.append("samples \n label\tfragments\tproduct\n");
            fragments = new StringBuilder();
         //   finalAssemble = new StringBuilder();
          
        } else {
            source = new StringBuilder(currStrings.get(0));
            samples = new StringBuilder(currStrings.get(1));
            destination = new StringBuilder(currStrings.get(2));
        }
        current_sample_num++;
        
        String dna = thread + current_sample_num + "p";
        String label = thread;
        fragments.append(dna).append(",");
        String fragString = fragments.toString();
        int fragStringlength = fragString.length();
        
        Assembly assembly = (Assembly) step;
        //String product = String.format("oligo2-oligo1/%s", digestion.getProduct());
        String product = "";
        //String source = "box" + thread + "/" + thread + Integer.toString(current_sample_num);
        String location = inventory.get(product, -1.0).getKey();
        source.append(dna + "\t").append(location).append("\n");
        //samples.append(dna + "\t").append(label + "p\t").append(source + "\t").append(product + "\n");
        samples.append(label + "\t").append(fragString.substring(0, fragStringlength - 1) + "\t")
            .append(assembly.getProduct() + "\n");
            
                
        
        System.out.println("Assembly:");
        System.out.println(samples.toString());
        List<String> newCurrStrings = new ArrayList<String>(Arrays.asList(source.toString(), samples.toString(), destination.toString()));
        return newCurrStrings;
    }
}