package org.ucb.c5.labsheet;

import org.ucb.c5.constructionfile.model.Digestion;
import org.ucb.c5.constructionfile.model.Step;

import java.util.List;

public class DigestionSheetGenerator {

    public void initiate() throws Exception {

    }


    public String run(List<Step> steps,  Inventory inventory, String thread) throws Exception {
        StringBuilder samples = new StringBuilder();
        samples.append("samples: \nlabel\tdna\tsource\tproduct\n");
        StringBuilder destination = new StringBuilder();
        destination.append("destination:");

        int currentSampleNum = 1;
        for (Step step : steps) {
            String label = thread + currentSampleNum;
            Digestion digestion = (Digestion) step;
            String product = String.format("oligo2-oligo1/%s", digestion.getProduct());
            samples.append(label + "\t").append(label + "p\t")
                    .append("box" + thread + "/" + thread + Integer.toString(currentSampleNum)+ "\t")
                    .append(product);
            currentSampleNum++;
        }

        return samples.toString() + destination;
    }
}
