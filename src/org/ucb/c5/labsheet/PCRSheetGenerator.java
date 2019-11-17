package org.ucb.c5.labsheet;

import org.ucb.c5.constructionfile.model.PCR;
import org.ucb.c5.constructionfile.model.Step;

import java.util.List;

public class PCRSheetGenerator { //, Inventory
    public static String run(List<Step> steps) {
        StringBuilder samples = new StringBuilder();
        samples.append("samples: \n label\tprimer1\tprimer2\ttemplate\tproduct\n");
        StringBuilder source = new StringBuilder();
        source.append("source: \n label\tlocation\tnote\n");
        StringBuilder destination = new StringBuilder();
        destination.append("destination:");
        
        StringBuilder notes1 = new StringBuilder();
        StringBuilder dilutionDests = new StringBuilder();
        StringBuilder notes2 = new StringBuilder();

        for (Step step: steps) {
            PCR pcrstep = (PCR) step;
            //creating separate strings for each heading within the PCRSheet
            samples.append((pcrstep.getOligo1()));
            samples.append((pcrstep.getOligo2()));

            //source.add(step.get(source))
            //destination.add(step.get(destination))
            //notes1 = ...
            //dilutionDest

            //
        }
        String PCRSheet = samples.toString() + source.toString() +
                destination.toString() + notes1.toString() + dilutionDests.toString() + notes2.toString();
        return PCRSheet;
    }


}
