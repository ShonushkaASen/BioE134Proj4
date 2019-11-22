package org.ucb.c5.Inventory;
import javafx.util.Pair;
import org.ucb.c5.Inventory.Box;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.ucb.c5.Inventory.ParseBoxFile;

public class Inventory{
	private List<Box> boxes;
	private HashMap<Experimentalthrd, Box> threadtobox;
	public void initiate() throws Exception {
		//Path to the folder containing all the files.
		File folder = new File("C:\\Users\\Arjun Chandran\\Documents\\BioE 134\\Proj4Files\\inventory");
		File[] listofboxes = folder.listFiles();
		threadtobox = new HashMap();
		boxes = new ArrayList<>();
		for(File file: listofboxes){
			String filename = file.getName();
			ParseBoxFile parser = new ParseBoxFile();
			parser.initiate();
			if(filename.contains(".DS")) {
				continue;
			}
			Box curr = parser.run(filename);
			//We need the box's thread attribute in a seperate class
			Experimentalthrd currthrd = curr.getBoxThread();
			threadtobox.put(currthrd, curr);
			boxes.add(curr);
		}
	}
	//Maybe include thread info here to eliminate the need to iterate. 
	public Pair<String, String> get(String name, Double con){
		//output is Pair<string, string> where first string is location, second string is a dilution note
		Location currloc = null;
		String lab_loc = null;
		Box desired = null;
		String part2 = new String("");
		for(Box box: boxes){
			if (box.containsName(name)){
				HashMap<Double, Location> loc = box.get(name);
				if (loc.containsKey(con)){
					currloc = loc.get(con);
					lab_loc = box.getLabLocation();
					desired = box;
					break;
				} else {
					Set<Double> concentrations = loc.keySet();
					currloc = loc.get(-1.0);
					lab_loc = box.getLabLocation();
					concentrations.remove(-1.0);
					if (concentrations.isEmpty()){
						//for now I am just going to print out these outputs... later on we can workout how we wanna handle these.
						System.out.println("There were no documented concentrations for this object");
						desired = box;
						// -1 since that what we decided for those that have a null concentration
					} else{
						for(Double curr: concentrations){
							//Concentration class should have a to Integer method
							if (curr % con == 0){
								currloc = loc.get(curr);
								lab_loc = box.getLabLocation();
								desired = box;
							} else {
								currloc = loc.get(curr);
								lab_loc = box.getLabLocation();
								desired = box;
							}
							Double a = curr;
							Double b = con;
							//make a dilution object that higher levels can look for to output the below print statement
							part2 = new String(a/b + " X solution of " + name + " needs to be made");
						}
						
					}
					
				}
			}
			continue;
		} 
		if (currloc == null && lab_loc == null){
			throw new Error("The thing you want does not exist in the inventory");
		}
		StringBuilder returnable = new StringBuilder();
		returnable.append(lab_loc + "/");
		returnable.append(desired.getBoxName() + "/");
		returnable.append(currloc.getRow() + "/" + currloc.getCol());
		String part1 = new String(returnable.toString());
		Pair returned = new Pair(part1, part2);
		return returned;
	}
	public String put(String name, Double concentration, String currthrd){
		//return location placed
		Experimentalthrd thrd = new Experimentalthrd(currthrd);
		Box currplace = threadtobox.get(thrd);
		//box itself should have a put method that handles the internals of changing that box's data
		currplace.put(name, concentration);
		Pair<String, String> loc = get(name, concentration);
		return loc.getKey();
	}
	public void write(){
		//Updates the Inventory by creating updated copies of Inventory files
		for(Box box: boxes){

		}
	}
	public static void main(String[] args) throws Exception {
		Inventory in = new Inventory();
		in.initiate();
		String oligo = "T4L1";
		Double concen = 1.0;
		Pair<String, String> loc = in.get(oligo, concen);
		System.out.println(loc.getKey());
		System.out.println(loc.getValue());
	}
}