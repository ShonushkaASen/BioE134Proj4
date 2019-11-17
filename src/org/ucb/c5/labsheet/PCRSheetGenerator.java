package org.ucb.c5.labsheet;

import org.ucb.c5.constructionfile.model.PCR;
import org.ucb.c5.constructionfile.model.Step;

import java.util.List;

public class PCRSheetGenerator { //, Inventory
    public static String run(List<Step> steps) {
        String samples = null;
        String source = null;
        String destination = null;
        String notes1 = null;
        String dilutionDests = null;
        String notes2 = null;

        for (Step step: steps) {
            PCR pcrstep = (PCR) step;
            //creating separate strings for each heading within the PCRSheet
            //samples.append(step.get(samples))
            //source.add(step.get(source))
            //destination.add(step.get(destination))
            //notes1 = ...
            //dilutionDest

            //
        }
        return null;
    }


}
