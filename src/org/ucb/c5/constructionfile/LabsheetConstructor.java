package org.ucb.c5.constructionfile;

import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.protocol.model.Step;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;


public class LabsheetConstructor {

    private HashMap<String, List<Step>> steps;
    private FileWriter fw;

    public void initiate() throws Exception {

    }

    public void run(ConstructionFile cf) throws Exception {
        File file = new File("Desktop/construction.doc");
        fw = new FileWriter(file);

        steps = new HashMap<>();

        for (String name : steps.keySet()) {
            if (name.equals("pcr")) {
//                step.get().writePCRSheet;
            } else if (name.equals("digest")) {

            } else if (name.equals("pca")) {

            } // add other steps
        }

        fw.close();
    }

    private void writePCRSheet() {

    }
}
