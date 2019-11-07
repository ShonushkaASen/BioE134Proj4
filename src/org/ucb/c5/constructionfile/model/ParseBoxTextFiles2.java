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
    private ArrayList<HashMap>[][] boxGrid;
    private String name;
    private String description;
    private String lab_location;
    private String temperature;
    private Queue emptySpots;
    private HashMap<Name, HashMap<Concentration, Location>> nameToConcToLoc;


    public void initiate() {
        nameToConcToLoc = new HashMap();
        boxGrid = new ArrayList[9][9];
        alphabet = new HashMap();
        nameToConcToLoc = new HashMap();
        alphabet.put(1, "A");
        alphabet.put(2, "B");
        alphabet.put(3, "C");
        alphabet.put(4, "D");
        alphabet.put(5, "E");
        alphabet.put(6, "F");
        alphabet.put(7, "G");
        alphabet.put(8, "H");
        alphabet.put(9, "I");
    }


    public void run(String box_file) throws Exception {
        Path filePath = Paths.get("/Users/sylviaillouz/Desktop/bioe134/constructionfile-and-protocol-demo-sylviaillouz/Proj4Files/inventory/"+ box_file +".txt");
        String data = Files.readString(filePath);

        String[] sections = data.split(">>");
        HashMap<String, String> attributeMap;
        parseHeader(sections[0]);

        for (int i = 1; i <= sections.length - 1; i++) {
            String section = sections[i];
            String[] lines = section.split("\\r|\\r?\\n");
            String[] header = lines[0].split("\t");
            String attribute = header[0];

            for (int j = 2; j < lines.length; j = j + 2) {
                if (lines[j].matches("[0-9]+")) {
                    continue;
                } else {
                    String line = lines[j];
                    String[] tabs = line.split("\t");
                    System.out.println(tabs[0] + tabs[1]);
                }


                /*int k = 1;
                for (String tab : tabs) {
                        attributeMap = new HashMap();
                        attributeMap.put(attribute, tab);
                        boxGrid[j][k].add(attributeMap);
                        k = k + 1;
                } */
            }
        }
        //iterate through newly created boxgrid to find 1) empty spaces 2) populate map <comp map <concent, loc>>
        for (int row = 0; row < boxGrid.length; row++) {
            for (int col = 0; col < boxGrid[row].length; col++) {
                if((boxGrid[row][col]).isEmpty()) {
                    emptySpots.add(new Location(row,col));
                } else {
                    populateNameMap(boxGrid[row][col], row, col);
                }

            }
        }

    }
    private void populateNameMap(ArrayList<HashMap> attribute_maps, int row, int col) {
        for (int i = 0; i < attribute_maps.size(); i++) {
           HashMap<String, String> curr_map = attribute_maps.get(i);
           Name name;
           Concentration concentration;
           if(curr_map.containsKey("composition")) {
               name = new Name(curr_map.get("composition"));
           } else {
               name = new Name(curr_map.get("name"));
           }
           if (curr_map.containsKey("concentration")) {
               concentration = new Concentration(Integer.parseInt(curr_map.get("concentration")));
           } else {
               // if no concentration data available, concentration set to value -1
               concentration = new Concentration();
           }
           Location location = new Location(row, col);
           HashMap<Concentration, Location> concToLoc = new HashMap();
           concToLoc.put(concentration, location);
           nameToConcToLoc.put(name, concToLoc);
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
                name = tabs[1];
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
        ParseBoxTextFiles2 parser = new ParseBoxTextFiles2();
        parser.initiate();
        parser.run("boxL");

    }
}

