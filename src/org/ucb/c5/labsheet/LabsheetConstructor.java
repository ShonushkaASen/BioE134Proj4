package org.ucb.c5.labsheet;

import org.ucb.c5.Inventory.Inventory;
import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.constructionfile.model.Operation;
import org.ucb.c5.constructionfile.model.Step;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import org.ucb.c5.constructionfile.ParseConstructionFile;
import org.ucb.c5.constructionfile.model.Acquisition;
import org.ucb.c5.constructionfile.model.Thread;

public class LabsheetConstructor {

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
    private List<String> DigestionString;
    private List<String> AssembleString;
    private List<List<String>> sheets = new ArrayList<>();


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
        //String thread_val = "B";
        ArrayList<String> labsheet = new ArrayList<>();
        for (ConstructionFile cf : cfs) {
            String plasmidName = cf.getPlasmid();
            for (Step step : cf.getSteps()) {
                Operation op = step.getOperation();
                switch(op) {
                    case acquire:
                        //acquire means we must put the product in inventory
                        Acquisition acquire = (Acquisition) step;
                        inventory.put(acquire.getProduct(), -1.0, "box" + thread_val);
                        break;
                    case pcr:
                        //PCRSheetGenerator takes in a  PCR step
                        PCRString = PCRSheetGenerator.run(step, inventory, thread_val, PCRString, plasmidName); //Map<String,String>;
                        sheets.add(PCRString);
                        break;
                    case digest:
                        DigestionString = DigestionSheetGenerator.run(step, inventory, thread_val, DigestionString, plasmidName);
                        sheets.add(DigestionString);
                        break;
                    case ligate:
                        LigationString = LigationSheetGenerator.run(step, inventory, thread_val, LigationString, plasmidName);
                        sheets.add(LigationString);
                        break;
                    case assemble:
                         AssembleString = AssembleSheetGenerator.run(step, inventory, thread_val, AssembleString);
                         sheets.add(AssembleString);
                        break;
                    case cleanup:
                        PCRCleanupString = PCRCleanupSheetGenerator.run(step, inventory, thread_val, PCRCleanupString, plasmidName);
                        sheets.add(PCRCleanupString);
                        break;
                    case transform:
                        TransformString = TransformSheetGenerator.run(step, inventory, thread_val, TransformString, plasmidName);
                        PlateString = PlateSheetGenerator.run(step, inventory, thread_val, PlateString, plasmidName);
                        PickString = PickSheetGenerator.run(step, inventory, thread_val, PickString, plasmidName);
                        MiniprepString = MiniprepSheetGenerator.run(step, inventory, thread_val, MiniprepString, plasmidName);
                        sheets.add(TransformString);
                        sheets.add(PlateString);
                        sheets.add(PickString);
                        sheets.add(MiniprepString);
                        break;
                    default:
                        //throw new Exception(op.toString() + "Operation not found to make sheet");
                        System.out.println("operation not found to make sheet");
                }
            }
            writeSheetsToFile();
        }
    }

    private void writeSheetsToFile() throws Exception {
        File file = new File("..\\src\\org\\ucb\\c5\\labsheet\\labsheetOutput\\construction.doc");
        fw = new FileWriter(file);
        for (List<String> sheet : sheets) {
            for (String attribute : sheet) {
                fw.write(attribute + "\f");
            }
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
