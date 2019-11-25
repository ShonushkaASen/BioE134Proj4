package org.ucb.c5.Inventory;
import javafx.util.Pair;
import org.ucb.c5.Inventory.Box;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.ucb.c5.Inventory.ParseBoxFile;

public class Inventory{
	private List<Box> boxes;
	private HashMap<String, Box> nametobox;
	public void initiate() throws Exception {
		//Path to the folder containing all the files.
		Path folderpath = Paths.get("inventory");
		File folder = new File("src/org/ucb/c5/Inventory/Proj4Files/inventory");
		//Converts Files in folder to a list of folders to iterate through
		File[] listofboxes = folder.listFiles();
		//Hashmap for easy look up of what box to put something in when storing a new item
		nametobox = new HashMap();
		boxes = new ArrayList<>();
		//Loop iterates through all files and generates appropriate box classes for each using Sylvia's parser
		for(File file: listofboxes){
			String filename = file.getName();
			//Parser is re instantiated everytime, its a dirty fix but the real fix needs to be done to the parser code and how it initializes cariables
			ParseBoxFile parser = new ParseBoxFile();
			parser.initiate();
			//Need to skip the .DS file, ultimately needs to be changed to a regex expression that recognizes and discards file names that start with a dot
			if(filename.contains(".DS")) {
				continue;
			}
			//current boc being generated
			Box curr = parser.run(filename);
			//We need the box's thread attribute in a seperate class
			String currname = curr.getBoxName();
			nametobox.put(currname,curr);
			//all boxes are then stored in a list for later look up in the inventory.
			boxes.add(curr);
		}
	}
	//Maybe include thread info here to eliminate the need to iterate. 
	public Pair<String, String> get(String name, Double con){
		//output is Pair<string, string> where first string is location, second string is a dilution note
		//This variable represents the row/col location of an object inside its box
		Location currloc = null;
		//this represents the location of the box in the lab
		String lab_loc = null;
		//this represents the thing we want is stored in
		Box desired = null;
		//this is just a null instantiation of the string that tell you if a dilution needs to be done or not
		String part2 = new String("");
		for(Box box: boxes){
			//checks if inventory contains the thing we want to look up
			if (box.containsName(name)){
				HashMap<Double, Location> loc = box.get(name);
				//once we know the thing we want to look up exists in the inventory this checks if the concentration we want is also there
				if (loc.containsKey(con)){
					currloc = loc.get(con);
					lab_loc = box.getLabLocation();
					desired = box;
					break;
				//if the concentration we want does not exist this looks for the easiest dilution
				} else {
					Set<Double> concentrations = loc.keySet();
					currloc = loc.get(-1.0);
					lab_loc = box.getLabLocation();
					concentrations.remove(-1.0);
					//checks if there are any concentrations available for this item
					if (concentrations.isEmpty()){
						//Lets you know if no concentration is available.
						System.out.println("There were no documented concentrations for this object");
						desired = box;
						// -1 since that what we decided for those that have a null concentration
					//finds easiest dilution
					} else{
						//curr represents the current concentration the loop is looking at
						//currloc represents the location of that concentration of whatever
						//lab_loc represents where in the lab this concentration of something is
						//desired represents the box within which this concentration of something is located
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
		returnable.append((numbertorowconverter(Double.parseDouble(currloc.getRow())) + "/" + (Double.parseDouble(currloc.getCol()) + 1.0)));
		String part1 = new String(returnable.toString());
		Pair returned = new Pair(part1, part2);
		return returned;
	}
	public String put(String name, Double concentration, String boxname){
		//return location placed
		Box currplace = nametobox.get(boxname);
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
	//converts what the parser spits out into human understandable rows
	private String numbertorowconverter(Double row){
		HashMap<Double, String> conversion = new HashMap<>();
		conversion.put(0.0, "A");
		conversion.put(1.0, "B");
		conversion.put(2.0, "C");
		conversion.put(3.0, "D");
		conversion.put(4.0, "E");
		conversion.put(5.0, "F");
		conversion.put(6.0, "G");
		conversion.put(7.0, "H");
		conversion.put(8.0, "I");
		conversion.put(9.0, "J");
		return conversion.get(row);
	}
	public static void main(String[] args) throws Exception {
		Inventory in = new Inventory();
		in.initiate();
		String oligo = "cadA pcrs";
		Double concen = 1.0;
		Pair<String, String> loc = in.get(oligo, concen);
		String loc23 = in.put("Arjun", 10.0, "boxO");
		System.out.println(loc.getKey());
		System.out.println(loc.getValue());
	}
}