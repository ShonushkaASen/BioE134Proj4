package org.ucb.c5.labsheet;

import Inventory;
import java.util.List;
import org.ucb.c5.constructionfile.model.Ligation;
import org.ucb.c5.constructionfile.model.Step;

public class LigationSheetGenerator {
    public void initiate() throws Exception {

    }
    
    public static String run(List<Step> steps, Inventory inventory, String thread) throws Exception {
        int current_sample_num = 0;
        StringBuilder samples = new StringBuilder();
        StringBuilder reaction = new StringBuilder();
        StringBuilder destination = new StringBuilder();
        samples.append("samples: \n label\tdigest\tsource\tproduct\n");
        reaction.append("reaction: \n7.5µL ddH2O\n1µL T4 DNA Ligase Buffer\n1µL DNA\n0.5µL T4 DNA Ligase\n");
        destination.append("destination: thermocycler_placeholder\n");
        
        for (Step step : steps) {
            current_sample_num++;
            String label = thread + Integer.toString(current_sample_num);
            String digest = label + "d";
            Ligation ligation = (Ligation) step;
            //String source = inventory.get(transform.getProduct());
            String product = ligation.getProduct() + "/lig";

            //to get locations, the put() method of inventory should return the location
            samples.append(label).append("\t").append(digest).append("\t").append("source_placeholder").append("\t").append(product).append("\n");
        }
        return reaction.toString() + "\n" + samples.toString() + "\n" + destination.toString();
    }
}
