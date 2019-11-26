package org.ucb.c5.Inventory;

import org.ucb.c5.utils.FileUtils;

import java.util.*;

/**
 *
 * @author Sylvia Illouz
 * This parser takes tab deliniated text files for boxes in the wet lab and using their data, populates box Objects
 * Each box object has a bunch of meta data (temperature, thread, etc), a map that makes retrieval of location easy,
 * and contains a queue of all open spots in the box (meaning that there is no current composition present at that
 * location in the box)
 */

public class ParseBoxFile {
    public ParseBoxFile() {

    }
    HashMap<String, Integer> alphabet;
    private HashMap[][] boxGrid;
    private String box_name;
    private String thread;
    private String description;
    private String lab_location;
    private String temperature;
    private Queue<Location> emptySpots;
    private HashMap<String, HashMap<Double, Location>> nameToConcToLoc;

    public void initiate() {
        nameToConcToLoc = new HashMap();
        boxGrid = new HashMap[10][10];
        alphabet = new HashMap();
        nameToConcToLoc = new HashMap();
        emptySpots = new ArrayDeque<>();
        alphabet.put("A", 1);
        alphabet.put("B", 2);
        alphabet.put("C", 3);
        alphabet.put("D", 4);
        alphabet.put("E", 5);
        alphabet.put("F", 6);
        alphabet.put("G", 7);
        alphabet.put("H", 8);
        alphabet.put("I", 9);
        alphabet.put("J", 10);
    }


    public Box run(String box_file) throws Exception {
        // read the file and create a string containing all text
        //Path filePath = Paths.get("/Users/sylviaillouz/Desktop/bioe134/constructionfile-and-protocol-demo-sylviaillouz/Proj4Files/inventory/" + box_file + ".txt");
        String data = FileUtils.readFile("src/org/ucb/c5/Inventory/Proj4Files/inventory/" + box_file);

        //splitting text by >> characters that separate sections of the file based on an attribute (composition, concentration, label)
        String[] sections = data.split(">>");
        parseHeader(sections[0]);

        //iterating through each section and populating a grid of hashmaps from attribute: value, that represents the contents of the lab box
        for (int i = 1; i < sections.length; i++) {
            String section = sections[i];
            String[] lines = section.split("\\r|\\r?\\n");

            // the first line of every section will be it's header, containing no real data, thus we handle it separately
            String[] header = lines[0].split("\t");
            String attribute = header[0];

            //if the line is completely empty, aka does not represent any space in a box (either empty box spots or filled with tubes), don't address it
            for (int j = 1; j < lines.length; j++) {
                String[] tabs;
                if (lines[j].isEmpty()) {
                    continue;
                }
                // for each row in the box, create a mapping of the attribute to its value and place it respective row and column location in the box grid
                String row_indicator = lines[j].substring(0, 1);
                if (row_indicator.matches("[1-9]") || row_indicator.matches("[A-Z]")) {
                    String line = lines[j];
                    tabs = line.split("\t");
                    Integer rowNum;
                    if (row_indicator.matches("[1-9]")) {
                        rowNum = Integer.parseInt(row_indicator);
                    } else {
                        rowNum = alphabet.get(row_indicator);
                    }
                    for (int k = 1; k < tabs.length; k++) {
                        // if the tab isnt empty, there's data to capture (e.g a concentration, a name, a label, etc)
                        if (!tabs[k].isEmpty()) {
                            String tab = tabs[k];
                            HashMap<String, String> attributeMap = boxGrid[rowNum - 1][k - 1];

                            // if the tab is empty, add that spot of the box to the queue of empty spots
                            if (attributeMap == null) {
                                attributeMap = new HashMap();
                                boxGrid[rowNum - 1][k - 1] = attributeMap;
                            }

                            attributeMap.put(attribute, tab);
                        }
                    }
                }
            }
            //iterate through newly created boxGrid to find 1) empty spaces 2) populate map <comp map <concent, loc>>
            for (int row = 0; row < boxGrid.length; row++) {
                for (int col = 0; col < boxGrid[row].length; col++) {
                    if ((boxGrid[row][col]) == null) {
                        emptySpots.add(new Location(row, col));
                    } else {
                        populateNameMap(boxGrid[row][col], row, col);
                    }

                }
            }

        }
        //return box populated box object
        return new Box(box_name, thread, description, lab_location, temperature, emptySpots, nameToConcToLoc, boxGrid);
    }

    //this method takes each spot in the grid and its respective attribute map and populates the nameToConcToLoc map
    private void populateNameMap(HashMap<String, String> curr_map, int row, int col) {
        String name;
        Double concentration;
        if (curr_map.containsKey("composition")) {
            name = curr_map.get("composition");
        } else {
            name = curr_map.get("name");
        }
        if (curr_map.containsKey("concentration")) {
            String[] conc = curr_map.get("concentration").split(" ");
            if (!(conc[0].matches("[0-9]"))) {
                concentration = 10.0;
            }
            else {
                concentration = Double.valueOf(conc[0]);
            }
        } else {
            // if no concentration data available, concentration set to value -1
            concentration = -1.0;
        }

        Location location = new Location(row, col);
        HashMap<Double, Location> concToLoc = new HashMap();
        concToLoc.put(concentration, location);
        nameToConcToLoc.put(name, concToLoc);

    }

    //handles the special case of the first section of each box file containting general info about the box
    private void parseHeader(String section) {
        String[] lines = section.split("\\r|\\r?\\n");
        //iterate through header to populate box meta data
        for (int i = 0; i < lines.length; i = i + 1) {
            if (!(lines[i].startsWith(">"))) {
                continue;
            }
            String line = lines[i];

            String[] tabs = line.split("\t");
            if ((tabs[0].equals(">name")) || tabs[0].equals("<composition")) {
                box_name = tabs[1];
                if (box_name.contains("box")) {
                    thread = Character.toString(box_name.charAt(3));
                } else {
                    thread = box_name;
                }

            } else if (tabs[0].equals(">description")) {
                description = tabs[1];
            } else if (tabs[0].equals(">location")) {
                lab_location = tabs[1];
            } else if (tabs[0].equals(">temperature")) {
                temperature = tabs[1];
            }
        }
    }


    public static void main(String[] args) throws Exception {
        ParseBoxFile parser = new ParseBoxFile();
        parser.initiate();
        Box box = parser.run("boxB.txt");
        System.out.println(box.containsName("pTarget-cscR1-A"));
    }
}
