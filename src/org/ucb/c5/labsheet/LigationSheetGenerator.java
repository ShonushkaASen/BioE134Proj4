package org.ucb.c5.labsheet;

import org.ucb.c5.constructionfile.model.Ligation;
import org.ucb.c5.constructionfile.model.Step;

import java.util.List;

public class LigationSheetGenerator {

    public void initiate() throws Exception {

    }

    public String run(List<Step> steps,  String thread) throws Exception {
        StringBuilder samples = new StringBuilder();
        samples.append("samples: \nlabel\tdigest\tsource\tproduct\n");
        StringBuilder destination = new StringBuilder();
        destination.append("destination:");

        int currentSampleNum = 1;
        for (Step step : steps) {
            String label = thread + currentSampleNum;
            Ligation ligation = (Ligation) step;
            String product = String.format("oligo2-oligo1/%s", ligation.getProduct());

            samples.append(label + "\t").append(label + "d\t").append("box" + thread + "/letter_placeholder\t")
                    .append(product);

            currentSampleNum++;
        }

        return samples.toString() + destination;
    }
}
