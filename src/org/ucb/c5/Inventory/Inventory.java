package org.ucb.c5.Inventory;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.*;
import java.util.AbstractMap.*;
import org.ucb.c5.Inventory.Box;

import java.io.File;

import org.ucb.c5.Inventory.ParseBoxFile;

/**
 * @author Arjun Chandra: initiate(), get(), put()
 * @author Sylvia Illouz: write()
 */
public class Inventory{
	private List<Box> boxes;
	private HashMap<String, Box> threadtobox;
	public void initiate() throws Exception {
		//Path to the folder containing all the files.
		File folder = new File("/Users/sylviaillouz/Desktop/bioe134/constructionfile-and-protocol-demo-sylviaillouz/Proj4/BioE134Proj4/Proj4Files/inventory");
		File[] listofboxes = folder.listFiles();
		threadtobox = new HashMap();
		boxes = new ArrayList<>();
		for(File file: listofboxes) {
			String filename = file.getName();
			ParseBoxFile parser = new ParseBoxFile();
			parser.initiate();
			if(filename.contains(".DS")) {
				continue;
			}
			Box curr = parser.run(filename);
			//We need the box's thread attribute in a seperate class
			String currthrd = curr.getBoxThread();
			threadtobox.put(currthrd, curr);
			boxes.add(curr);
		}
	}
	//Maybe include thread info here to eliminate the need to iterate. 
	public SimpleEntry<String, String> get(String name, Double con) {
		//output is Pair<string, string> where first string is location, second string is a dilution note
		Location currloc = null;
		String lab_loc = null;
		Box desired = null;
		String part2 = new String("");
		for(Box box: boxes){
			if (box.containsName(name)) {
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
					if (concentrations.isEmpty()) {
						//for now I am just going to print out these outputs... later on we can workout how we wanna handle these.
						System.out.println("There were no documented concentrations for this object");
						desired = box;
						// -1 since that what we decided for those that have a null concentration
					} else{
						for(Double curr: concentrations) {
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
		SimpleEntry returned = new SimpleEntry(part1, part2);
		return returned;
	}
	public String put(String name, Double concentration, String currthrd){
		//return location placed
		Box currplace = threadtobox.get(currthrd);
		//box itself should have a put method that handles the internals of changing that box's data
		currplace.put(name, concentration);
		SimpleEntry<String, String> loc = get(name, concentration);
		return loc.getKey();
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
			data.append(">name").append("\t").append(box.getBoxName().toUpperCase()).append("\n");
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
				data.append(">>label").append("\t").append(columns).append("\n");
				data.append(labels.toString());
			}
			if (!names.toString().matches("[\t\n0-9]+")) {
				data.append(">>name").append("\t").append(columns).append("\n");
				data.append(names.toString());
			}
			if (!comps.toString().matches("[\t\n0-9]+")) {
				data.append(">>composition").append("\t").append(columns).append("\n");
				data.append(comps.toString());
			}
			if (!concs.toString().matches("[\t\n0-9]+")) {
				data.append(">>concentration").append("\t").append(columns).append("\n");
				data.append(concs.toString());
			}
			if (!clone.toString().matches("[\t\n0-9]+")) {
				data.append(">>clone").append("\t").append(columns).append("\n");
				data.append(clone.toString());
			}
			if (!host.toString().matches("[\t\n0-9]+")) {
				data.append(">>host").append("\t").append(columns).append("\n");
				data.append(host.toString());
			}
			if (!antibiotic.toString().matches("[\t\n0-9]+")) {
				data.append(">>antibiotic").append("\t").append(columns).append("\n");
				data.append(antibiotic.toString());
			}
			if (!note.toString().matches("[\t\n0-9]+")) {
				data.append(">>note").append("\t").append(columns).append("\n");
				data.append(note.toString());
			}
			System.out.println(data.toString());
			File file = new File("/Users/sylviaillouz/Desktop/bioe134/constructionfile-and-protocol-demo-sylviaillouz/Proj4/BioE134Proj4/Proj4Files/inventory/boxA.txt");
			FileWriter rewrite = new FileWriter(file, false);
			rewrite.write(data.toString());
			rewrite.close();
			break;
		}

	}

	public static void main(String[] args) throws Exception {
		Inventory in = new Inventory();
		in.initiate();
		String oligo = "T4L1";
		Double concen = 1.0;
		in.write();
		SimpleEntry<String, String> loc = in.get(oligo, concen);
		String locat = in.put("arjun", 10.0, "A");
		System.out.println(locat);
		System.out.println(loc.getKey());
		System.out.println(loc.getValue());
		in.write();
	}
}