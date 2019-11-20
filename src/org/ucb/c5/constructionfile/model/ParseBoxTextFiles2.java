package org.ucb.c5.constructionfile.model;

import org.knowm.xchart.internal.Utils;
import org.ucb.c5.utils.FileUtils;
import org.w3c.dom.Attr;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class ParseBoxTextFiles2 {
    private HashMap<Integer, String> alphabet;
    private HashMap[][] boxGrid;
    private String box_name;
    private String thread;
    private String description;
    private String lab_location;
    private String temperature;
    private Queue<Location> emptySpots;
    private HashMap<Name, HashMap<Concentration, Location>> nameToConcToLoc;


    public void initiate() {
        nameToConcToLoc = new HashMap();
        boxGrid = new HashMap[9][9];
        alphabet = new HashMap();
        nameToConcToLoc = new HashMap();
        emptySpots = new ArrayDeque<>();
        alphabet.put(2, "A");
        alphabet.put(4, "B");
        alphabet.put(6, "C");
        alphabet.put(8, "D");
        alphabet.put(10, "E");
        alphabet.put(12, "F");
        alphabet.put(14, "G");
        alphabet.put(16, "H");
        alphabet.put(18, "I");
    }


    public Box run(String box_file) throws Exception {
        Path filePath = Paths.get("/Users/sylviaillouz/Desktop/bioe134/constructionfile-and-protocol-demo-sylviaillouz/Proj4Files/inventory/" + box_file + ".txt");
        String data = Files.readString(filePath);

        String[] sections = data.split(">>");
        parseHeader(sections[0]);

        for (int i = 1; i <= sections.length - 1; i++) {
            String section = sections[i];
            String[] lines = section.split("\\r|\\r?\\n");
            String[] header = lines[0].split("\t");
            String attribute = header[0];

            for (int j = 2; j < lines.length; j = j + 2) {
                String[] tabs;
                if (lines[j].substring(1).matches("[\t]+")) {
                    continue;
                } else {
                    String line = lines[j];
                    tabs = line.split("\t");

                    for (int k = 1; k < tabs.length; k++) {
                        if (!tabs[k].isEmpty()) {
                            String tab = tabs[k];
                            HashMap<String, String> attributeMap = boxGrid[(j / 2) - 1][k - 1];

                            if (attributeMap == null) {
                                attributeMap = new HashMap();
                                boxGrid[(j / 2) - 1][k - 1] = attributeMap;
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
        return new Box(box_name, thread, description, lab_location, temperature, emptySpots, nameToConcToLoc);
    }
    private void populateNameMap(HashMap<String, String> curr_map, int row, int col) {
        Name name;
        Concentration concentration;
        if (curr_map.containsKey("composition")) {
            name = new Name(curr_map.get("composition"));
            System.out.println(name);
        } else {
            name = new Name(curr_map.get("name"));
        }
        if (curr_map.containsKey("concentration")) {
            String[] conc = curr_map.get("concentration").split(" ");
            concentration = new Concentration(Integer.valueOf(conc[0]));
        } else {
            // if no concentration data available, concentration set to value -1
            concentration = new Concentration();
        }
        Location location = new Location(row, col);
        HashMap<Concentration, Location> concToLoc = new HashMap();
        concToLoc.put(concentration, location);
        nameToConcToLoc.put(name, concToLoc);
        if (!(name.getName() == null)) {
            System.out.println(" name: " + name.getName() + " concentration: " + concentration.getConcentration() + " location: " + location.getCol() + " " + location.getRow());
        }
    }




    private void parseHeader(String section) {
        String[] lines = section.split("\\r|\\r?\\n");
        //iterate through header to populate box meta data
        for (int i = 0; i < lines.length; i=i+1) {
            if (!(lines[i].startsWith(">"))) {
                continue;
            }
            String line = lines[i];

            String[] tabs = line.split("\t");
            if ((tabs[0].equals(">name")) || tabs[0].equals("<composition")) {
                box_name = tabs[1];
                thread = Character.toString(box_name.charAt(3));
                System.out.println(box_name);
            } else if (tabs[0].equals(">description")) {
                description = tabs[1];
                System.out.println(description);
            } else if (tabs[0].equals(">location")) {
                lab_location = tabs[1];
                System.out.println(lab_location);
            } else if (tabs[0].equals(">temperature")) {
                temperature = tabs[1];
                System.out.println(temperature);
            }
        }
    }


    public static void main(String[] args) throws Exception {
        ParseBoxTextFiles2 parser = new ParseBoxTextFiles2();
        parser.initiate();
        Box box = parser.run("boxL");
    }
}

