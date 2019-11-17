package org.ucb.c5.labsheet;

import org.ucb.c5.composition.model.Construct;
import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.constructionfile.model.Operation;
import org.ucb.c5.constructionfile.model.Step;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LabsheetConstructor {

    private HashMap<Operation, List<Step>> stepMap;
    private FileWriter fw;

    public void initiate() throws Exception {

    }
    //creating multiple labSheets for the number of construction files in cfs
    public void run(List<ConstructionFile> cfs) throws Exception {
        //doing the same string creation operation for the number of labsheets (i.e. outputting one file for ALL operations)
        File file = new File("Desktop/construction.doc");
        fw = new FileWriter(file);
        ArrayList<String> labsheet = new ArrayList<>();
        for (ConstructionFile cf : cfs) {
            //populating a hashmap (e.g 'PCR' --> List<PCR>) that splits all steps of the same type
            stepMap = new HashMap<>();
            for (Step step : cf.getSteps()) {
                Operation op = step.getOperation();
                if (stepMap.containsKey(op)) {
                    stepMap.get(op).add(step);
                } else {
                    ArrayList<Step> stepList = new ArrayList<>();
                    stepList.add(step);
                    stepMap.put(op, stepList);
                }
            }


            for (Operation op : stepMap.keySet()) {
                List<Step> steps = stepMap.get(op);
                String sheet = null;
                switch(op) {
                    case pcr:
                        //PCRSheetGenerator takes in a list of PCR steps
                        sheet = PCRSheetGenerator.run(steps); //Map<String,String>;

                 //           PCR pcrstep = (PCR) step;
                        break;
                    case pca:
                        break;
                }
                labsheet.add(sheet);
            }

            fw.close();
        }
    }

    private void writePCRSheet() {

    }
}
