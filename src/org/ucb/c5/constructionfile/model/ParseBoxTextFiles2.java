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
    private HashMap<String, Integer> alphabet;
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
        boxGrid = new HashMap[10][9];
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
        Path filePath = Paths.get("/Users/sylviaillouz/Desktop/bioe134/constructionfile-and-protocol-demo-sylviaillouz/Proj4Files/inventory/" + box_file + ".txt");
        String data = Files.readString(filePath);

        String[] sections = data.split(">>");
        parseHeader(sections[0]);

        for (int i = 1; i < sections.length; i++) {
            String section = sections[i];
            String[] lines = section.split("\\r|\\r?\\n");
            for (int a = 0; a < lines.length; a++) {
                System.out.print(a + ": ");
                System.out.print(lines[a]);
                System.out.println();
            }
            String[] header = lines[0].split("\t");
            String attribute = header[0];

            for (int j = 1; j < lines.length; j++) {
                String[] tabs;
                if (lines[j].isEmpty()) {
                    continue;
                }
                String row_indicator = lines[j].substring(0, 1);
                System.out.println(row_indicator);
                if (row_indicator.matches("[1-9]") || row_indicator.matches("[A-Z]")) {
                    String line = lines[j];
                    tabs = line.split("\t");
                    Integer rowNum;
                    if (row_indicator.matches("[1-9]")) {
                        rowNum = Integer.parseInt(row_indicator);
                    } else {
                        rowNum = alphabet.get(row_indicator);
                    }
                    System.out.println(rowNum);
                    for (int k = 1; k < tabs.length; k++) {
                        if (!tabs[k].isEmpty()) {
                            String tab = tabs[k];
                            HashMap<String, String> attributeMap = boxGrid[rowNum - 1][k - 1];

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
        return new Box(box_name, thread, description, lab_location, temperature, emptySpots, nameToConcToLoc);
    }

    private void populateNameMap(HashMap<String, String> curr_map, int row, int col) {
        Name name;
        Concentration concentration;
        if (curr_map.containsKey("composition")) {
            name = new Name(curr_map.get("composition"));
        } else {
            name = new Name(curr_map.get("name"));
        }
        if (curr_map.containsKey("concentration")) {
            String[] conc = curr_map.get("concentration").split(" ");
            concentration = new Concentration(Double.valueOf(conc[0]));
        } else {
            // if no concentration data available, concentration set to value -1
            concentration = new Concentration();
        }
        Location location = new Location(row, col);
        HashMap<Concentration, Location> concToLoc = new HashMap();
        concToLoc.put(concentration, location);
        nameToConcToLoc.put(name, concToLoc);
        if (!(name.getName() == null)) {
            System.out.println(" name: " + name.getName() + " concentration: " + concentration.getConcentration() + " location: " + location.getRow() + " " + location.getCol());
        }
    }


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
        Box box = parser.run("boxA");
        Queue<Location> queue = box.getEmptySpots();
        for (Location s : queue) {
            System.out.println("empty: " + s.getRow() + " " + s.getCol());
        }
    }
}

