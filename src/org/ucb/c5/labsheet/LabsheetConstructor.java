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

/**
 * @author Heloise Carion, Katlyn Ho, Sina Ghandian
 */

public class LabsheetConstructor {

    private FileWriter fw;
    private String filepath;
    private Inventory inventory;
    private Thread thread;
    
    //we will recursively add to these lists of strings as we encounter each corresponding operation
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


    // construction files need to have all of the steps listed or else they won't show up on the labsheet
    public void run(List<ConstructionFile> cfs, String filepath) throws Exception {
        this.filepath = filepath;
        //thread.get() will return the next available thread value
        String thread_val = thread.get();
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
                        //we pass in PCRString as an argument, then reassign in to the output of PCRSheetGenerator
                        //PCRSheetGenerator should preserve the previous state of PCRString but append to the Strings 
                        //with additional information
                        PCRString = PCRSheetGenerator.run(step, inventory, thread_val, PCRString, plasmidName); //Map<String,String>;
                        break;
                    case digest:
                        DigestionString = DigestionSheetGenerator.run(step, inventory, thread_val, DigestionString, plasmidName);
                        break;
                    case ligate:
                        LigationString = LigationSheetGenerator.run(step, inventory, thread_val, LigationString, plasmidName);
                        break;
                    case assemble:
                         AssembleString = AssembleSheetGenerator.run(step, inventory, thread_val, AssembleString, plasmidName);
                         sheets.add(AssembleString);
                        break;
                    case cleanup:
                        PCRCleanupString = PCRCleanupSheetGenerator.run(step, inventory, thread_val, PCRCleanupString, plasmidName);
                        break;
                    case transform:
                        //in the case of a transformation, we know that this step must always be followed by a Plate, Pick, and Miniprep
                        //step in that order
                        TransformString = TransformSheetGenerator.run(step, inventory, thread_val, TransformString, plasmidName);
                        PlateString = PlateSheetGenerator.run(step, inventory, thread_val, PlateString, plasmidName);
                        PickString = PickSheetGenerator.run(step, inventory, thread_val, PickString, plasmidName);
                        MiniprepString = MiniprepSheetGenerator.run(step, inventory, thread_val, MiniprepString, plasmidName);
                        break;
                    default:
                        System.out.println("operation not found to make sheet");
                }
            }
            //after all steps from all construction files have been processed, we add the final step Strings to "sheets"
            //which will then be written to a .doc file by writeSheetsToFile();
            sheets.add(PCRString);
            sheets.add(PCRCleanupString);
            sheets.add(DigestionString);
            sheets.add(LigationString);
            sheets.add(TransformString);
            sheets.add(PlateString);
            sheets.add(PickString);
            sheets.add(MiniprepString);
            writeSheetsToFile();
        }
    }

    private void writeSheetsToFile() throws Exception {
        File file = new File(filepath);
        fw = new FileWriter(file);
        for (List<String> sheet : sheets) {
            if (sheet == null) {
                continue;
            }
            for (String attribute : sheet) {
                fw.write(attribute + "\n");
            }
            fw.write("\f");
        }
        fw.close();
    }
    
    public static void main(String[] args) throws Exception {
        LabsheetConstructor constructor = new LabsheetConstructor();
        constructor.initiate();
        ParseConstructionFile parser = new ParseConstructionFile();
        
        String data = ">Construction of pTargetF1\n" +
                "pcr yyBla-F,yyBla-R on p20N5\t(1183 bp, fragA)\n" +
                "cleanup\tfragA\t(fragA)\n" +
                "pcr yyEI-F,yyEI-R on pTargetF\t(1174 bp, fragB)\n" +
                "cleanup\tfragB\t(fragB)\n" +
                "assemble fragA,fragB\t(BsaI, pTarg1) \n" +
                "transform pTarg1\t(Mach1, Amp)";
        ConstructionFile cf = parser.run(data);
        List<ConstructionFile> list = new ArrayList<>();
        list.add(cf);
        String path = "construction_new.doc";
        constructor.run(list, path);
//        String s = "boxA A";
//        System.out.print(s.replace("null/", ""));
    }

}
