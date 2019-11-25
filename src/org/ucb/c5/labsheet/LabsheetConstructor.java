package org.ucb.c5.labsheet;

import org.ucb.c5.Inventory.Inventory;
import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.constructionfile.model.Operation;
import org.ucb.c5.constructionfile.model.Step;
import org.ucb.c5.constructionfile.model.Thread;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.ucb.c5.constructionfile.ParseConstructionFile;
import org.ucb.c5.constructionfile.model.Acquisition;


public class LabsheetConstructor {

    private ArrayList<Step> stepList;
    private FileWriter fw;
    private Inventory inventory;
    private Thread thread;
    
    private List<String> LigationString;
    private List<String> MiniprepString;
    private List<String> PCRCleanupString;
    private List<String> PCRString;
    private List<String> PickString;
    private List<String> PlateString;
    private List<String> TransformString;


    public void initiate() throws Exception {
        inventory = new Inventory();
        inventory.initiate();
        thread = new Thread();
        thread.initiate();
    }

    //creating multiple labSheets for the number of construction files in cfs
    public void run(List<ConstructionFile> cfs) throws Exception {
        //doing the same string creation operation for the number of labsheets (i.e. outputting one file for ALL operations)
        String thread_val = thread.get();
        ArrayList<String> labsheet = new ArrayList<>();
        for (ConstructionFile cf : cfs) {
            //populating a hashmap (e.g 'PCR' --> List<PCR>) that splits all steps of the same type
            for (Step step : cf.getSteps()) {
                stepList.add(step);
            }

            for (Step step : stepList) {
                Operation op = step.getOperation();
                switch(op) {
                    case acquire:
                        Acquisition acquire = (Acquisition) step;
                        inventory.put(acquire.getProduct(), -1.0, "box" + thread_val);
                        break;
                    case pcr:
                        //PCRSheetGenerator takes in a list of PCR steps
                        PCRString = PCRSheetGenerator.run(step, inventory, thread_val); //Map<String,String>;

                 //           PCR pcrstep = (PCR) step;
                        break;
                    case digest:
                        break;
                    case ligate:
//                        sheet = LigationSheetGenerator.run(steps, inventory, thread_val);
//                        break;
                    case assemble:
                        break;
                    case cleanup:
                        break;
                    case transform:
//                        sheet = TransformSheetGenerator.run(steps, inventory, thread_val);
//                        labsheet.add(sheet);
//                        sheet = PlateSheetGenerator.run(steps, inventory, thread_val);
//                        labsheet.add(sheet);
////                        sheet = PickSheetGenerator.run(steps, inventory, thread_val);
////                        labsheet.add(sheet);
//                        sheet = MiniprepSheetGenerator.run(steps, inventory, thread_val);
                        break;
                    default:
                        //throw new Exception(op.toString() + "Operation not found to make sheet");
                        System.out.println("operation not found to make sheet");
                }
                labsheet.add(sheet);
            }
            writeSheetsToFile(labsheet);
        }
    }

    private void writeSheetsToFile(ArrayList<String> labsheet) throws Exception {
        File file = new File("Desktop/construction.doc");
        fw = new FileWriter(file);
        for (String sheet : labsheet) {
            fw.write(sheet + '\f');
        }
        fw.close();
    }
    
    public static void main(String[] args) throws Exception {
        LabsheetConstructor constructor = new LabsheetConstructor();
        constructor.initiate();
        ParseConstructionFile parser = new ParseConstructionFile();
        
        String data = ">Construction of pTarget-cscB1\n" +
            "acquire oligo cscB1,pTargRev\n" +
            "acquire plasmid pTargetF\n" +
            "pcr cscB1,pTargRev on pTargetF	(3927 bp, ipcr)\n" +
            "cleanup ipcr	(pcrpdt)\n" +
            "digest pcrpdt with SpeI,DpnI	(spedig)\n" +
            "cleanup spedig	(dig)\n" +
            "ligate dig	(lig)\n" +
            "transform lig	(Mach1, Spec)";
        
        ConstructionFile cf = parser.run(data);
        List<ConstructionFile> list = new ArrayList<>();
        list.add(cf);
        constructor.run(list);
    }

}
