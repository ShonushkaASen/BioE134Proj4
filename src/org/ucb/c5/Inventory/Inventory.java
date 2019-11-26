package org.ucb.c5.Inventory;
import java.io.FileWriter;

import java.util.AbstractMap.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author Arjun Chandra: initiate(), get(), put()
 * @author Sylvia Illouz: write()
 */
public class Inventory {
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
                ParseBoxFile parser = new ParseBoxFile();
                parser.initiate();
		for (File file: listofboxes) {
		//Loop iterates through all files and generates appropriate box classes for each using Sylvia's parser

			String filename = file.getName();
			//Parser is re instantiated everytime, its a dirty fix but the real fix needs to be done to the parser code and how it initializes cariables
			
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
	public SimpleEntry<String, String> get(String name, Double con) {
		//output is Pair<string, string> where first string is location, second string is a dilution note
		//This variable represents the row/col location of an object inside its box
		Location currloc = null;
		//this represents the location of the box in the lab
		String lab_loc = null;
		//this represents the thing we want is stored in
		Box desired = null;
		//this is just a null instantiation of the string that tell you if a dilution needs to be done or not
		String part2 = new String("");
		for (Box box : boxes) {
				//checks if inventory contains the thing we want to look up
			if (box.containsName(name)) {

				HashMap<Double, Location> loc = box.get(name);
				//once we know the thing we want to look up exists in the inventory this checks if the concentration we want is also there
				if (loc.containsKey(con)) {
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
					if (concentrations.isEmpty()) {
						//for now I am just going to print out these outputs... later on we can workout how we wanna handle these.
						//checks if there are any concentrations available for this item
							//Lets you know if no concentration is available.
						System.out.println("There were no documented concentrations for this object");
						desired = box;
							// -1 since that what we decided for those that have a null concentration
							//finds easiest dilution
					} else {
						for (Double curr : concentrations) {
							//curr represents the current concentration the loop is looking at
							//currloc represents the location of that concentration of whatever
							//lab_loc represents where in the lab this concentration of something is
							//desired represents the box within which this concentration of something is located
							//Concentration class should have a to Integer method
							if (curr % con == 0) {
								currloc = loc.get(curr);
								lab_loc = box.getLabLocation();
								desired = box;
							} else {
								currloc = loc.get(curr);
								lab_loc = box.getLabLocation();
								desired = box;
							}
							System.out.println("desired:" + box.getBoxName());
							//make a dilution object that higher levels can look for to output the below print statement
							part2 = "a dilution of " + (curr / con)  + "x needs to be made to achieve desired concentration of " + con + "uM";
						}
					}

				}
				break;
			}
		}

		if (currloc == null && lab_loc == null) {
			throw new Error("The thing you want does not exist in the inventory");
		}
		StringBuilder returnable = new StringBuilder();
		returnable.append(lab_loc + "/");
		returnable.append(desired.getBoxName() + "/");
		returnable.append((numbertorowconverter(Double.parseDouble(currloc.getRow())) + "/" + (Double.parseDouble(currloc.getCol()) + 1.0)));
		String part1 = new String(returnable.toString());
		SimpleEntry returned = new SimpleEntry(part1, part2);
		return returned;

	}
	public String put(String name, Double concentration, String boxname) {
		//return location placed
		Box currplace = nametobox.get(boxname);
		//box itself should have a put method that handles the internals of changing that box's data
		Location placeput = currplace.put(name, concentration);
		return currplace.getBoxName() + " " + numbertorowconverter(Double.parseDouble(placeput.getRow())) + "/" + (Double.parseDouble(placeput.getCol()));
	}

	 //recreation of box tab deliniated text files after inventory updated (post experiment)
	public void write() throws Exception {
		//Creates updated box text files of inventory current state
		List<File> outputs = new ArrayList<>();
		String columns = "A" + "\t" + "B" + "\t" + "C" + "\t" + "D" + "\t" + "E" + "\t" + "F" + "\t" + "G" + "\t" + "H" + "\t" + "I" + "\t" + "J";
		for (Box box : boxes) {
			StringBuilder data = new StringBuilder();
			HashMap[][] boxGrid = box.getBoxGrid();

			StringBuilder labels = new StringBuilder();
			StringBuilder comps = new StringBuilder();
			StringBuilder names = new StringBuilder();
			StringBuilder concs = new StringBuilder();
			StringBuilder note = new StringBuilder();
			StringBuilder host = new StringBuilder();
			StringBuilder antibiotic = new StringBuilder();
			StringBuilder clone = new StringBuilder();

			//header
			data.append(">name").append("\t").append(box.getBoxName()).append("\n");
			data.append(">description").append("\t").append(box.getBoxDescription()).append("\n");
			data.append(">location").append("\t").append(box.getLabLocation()).append("\n");
			data.append(">plate_type").append("\t").append("cardboard box").append("\n");
			data.append(">temperature").append("\t").append(box.getBoxTemperature()).append("\n");

			//specifics of each tube
			//labels.append(">>label").append("\t").append(columns).append("\n");
			//comps.append(">>composition").append("\t").append(columns).append("\n");
			int k = 1;
			List<String> attributes = new ArrayList();
			attributes.add("label");
			attributes.add("composition");
			attributes.add("name");
			attributes.add("concentration");
			attributes.add("note");
			attributes.add("host");
			attributes.add("antibiotic");
			attributes.add("clone");
			for (int row = 0; row < boxGrid.length; row++) {
				for (int col = 0; col < boxGrid[row].length; col++) {
					if (col == 0) {
						labels.append(k).append("\t");
						comps.append(k).append("\t");
						names.append(k).append("\t");
						concs.append(k).append("\t");
						note.append(k).append("\t");
						host.append(k).append("\t");
						antibiotic.append(k).append("\t");
						clone.append(k).append("\t");
						k++;
					}


					if (!(boxGrid[row][col] == null)) {
						if (boxGrid[row][col].containsKey("label")) {
							labels.append(boxGrid[row][col].get("label"));
						}
						if (boxGrid[row][col].containsKey("composition")) {
							comps.append(boxGrid[row][col].get("composition"));
						}
						if (boxGrid[row][col].containsKey("name")) {
							names.append(boxGrid[row][col].get("name"));
						}
						if (boxGrid[row][col].containsKey("concentration")) {
							concs.append(boxGrid[row][col].get("concentration"));
						}
						if (boxGrid[row][col].containsKey("note")) {
							note.append(boxGrid[row][col].get("note"));
						}
						if (boxGrid[row][col].containsKey("host")) {
							host.append(boxGrid[row][col].get("host"));
						}
						if (boxGrid[row][col].containsKey("antibiotic")) {
							antibiotic.append(boxGrid[row][col].get("antibiotic"));
						}
						if (boxGrid[row][col].containsKey("clone")) {
							clone.append(boxGrid[row][col].get("clone"));
						}
					}

					labels.append("\t");
					names.append("\t");
					comps.append("\t");
					concs.append("\t");
					note.append("\t");
					host.append("\t");
					antibiotic.append("\t");
					clone.append("\t");
				}
				labels.append("\n");
				comps.append("\n");
				names.append("\n");
				concs.append("\n");
				note.append("\n");
				host.append("\n");
				antibiotic.append("\n");
				clone.append("\n");
			}
			if (!labels.toString().matches("[\t\n0-9]+")) {
				data.append("\n").append(">>label").append("\t").append(columns).append("\n");
				data.append(labels.toString());
			}
			if (!names.toString().matches("[\t\n0-9]+")) {
				data.append("\n").append(">>name").append("\t").append(columns).append("\n");
				data.append(names.toString());
			}
			if (!comps.toString().matches("[\t\n0-9]+")) {
				data.append("\n").append(">>composition").append("\t").append(columns).append("\n");
				data.append(comps.toString());
			}
			if (!concs.toString().matches("[\t\n0-9]+")) {
				data.append("\n").append(">>concentration").append("\t").append(columns).append("\n");
				data.append(concs.toString());
			}
			if (!clone.toString().matches("[\t\n0-9]+")) {
				data.append("\n").append(">>clone").append("\t").append(columns).append("\n");
				data.append(clone.toString());
			}
			if (!host.toString().matches("[\t\n0-9]+")) {
				data.append("\n").append(">>host").append("\t").append(columns).append("\n");
				data.append(host.toString());
			}
			if (!antibiotic.toString().matches("[\t\n0-9]+")) {
				data.append("\n").append(">>antibiotic").append("\t").append(columns).append("\n");
				data.append(antibiotic.toString());
			}
			if (!note.toString().matches("[\t\n0-9]+")) {
				data.append("\n").append(">>note").append("\t").append(columns).append("\n");
				data.append(note.toString());
			}
			File file = new File("/Users/sylviaillouz/Desktop/bioe134/constructionfile-and-protocol-demo-sylviaillouz/Proj4/BioE134Proj4/src/org/ucb/c5/Inventory/Proj4Files/OutputInventory/" + box.getBoxName() + "updated.txt");
			FileWriter rewrite = new FileWriter(file, false);
			rewrite.write(data.toString());
			rewrite.close();
		}

	}

	//converts what the parser spits out into human understandable rows
	private String numbertorowconverter(Double row) {
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
		String oligo = "yyBla-F";
		Double concen = 5.0;
		SimpleEntry<String, String> loc = in.get(oligo, concen);
		System.out.println("returned: " + loc);
		String loc23 = in.put("Arjun", 10.0, "boxO");
		System.out.println(loc23);
		in.write();
	}
}