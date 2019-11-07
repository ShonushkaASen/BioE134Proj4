package org.ucb.c5.constructionfile;

import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.constructionfile.model.Step;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LabsheetConstructor {

    private HashMap<String, List<Step>> stepMap;
    private FileWriter fw;

    public void initiate() throws Exception {

    }

    public void run(ConstructionFile cf) throws Exception {
        File file = new File("Desktop/construction.doc");
        fw = new FileWriter(file);

        stepMap = new HashMap<>();
        for (Step step : cf.getSteps()) {
            String name = step.getClass().toString().substring(6);
            if (stepMap.containsKey(name)) {

            } else {
                ArrayList stepList = new ArrayList<>();
//                stepMap.put(name, stepList.add(step));  needs to ask johnny about
            }
        }

        for (String name : stepMap.keySet()) {
            List<Step> steps = stepMap.get(name);

            if (name.equals("pcr")) {
//                step.get().writePCRSheet(steps, fw);

            } else if (name.equals("digest")) {

            } else if (name.equals("pca")) {

            } // add other steps
        }

        fw.close();
    }

    private void writePCRSheet() {

    }
}
