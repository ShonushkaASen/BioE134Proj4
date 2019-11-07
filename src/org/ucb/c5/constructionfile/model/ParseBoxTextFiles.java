/* package org.ucb.c5.constructionfile.model;

import org.ucb.c5.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

public class ParseBoxTextFiles {
    private HashMap<Integer, String> alphabet;
    private HashMap<String, List<String>> labelToLocation;
    private Queue<String> emptyPositions;
    private String name;
    private String description;
    private String lab_location;

    public void initiate() throws Exception {
        this.labelToLocation = new HashMap();
        this.emptyPositions = new Queue();
        alphabet = new HashMap();
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

    public void run(File box_file) throws Exception {

        String data = FileUtils.readResourceFile("Proj4Files/inventory/" + box_file.toString() + ".txt");
        String[] lines = data.split("\\r|\\r?\\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String[] tabs = line.split("\t");
            if ((tabs[0]).equals(">name")) {
                name = tabs[1];
                continue;
            }
            if ((tabs[0]).equals(">description")) {
                description = tabs[1];
                continue;
            }
            if ((tabs[0]).equals("location")) {
                lab_location = tabs[1];
                continue;
            }
            if ((tabs[0]).equals(">>label")) {
                tabs = lines[i + 1].split("\t");
                while(tabs[0].matches("^\\d+$")){
                    label_helper(tabs, labelToLocation);
                }
                continue;
            }

        }
        // Box box = new Box(name, description, lab_location);
        // return box;
    }


    private void label_helper(String[] tabs, HashMap map) {
        for (int j = 1; j < 10; j++) {
            if (tabs[j].equals("")) {
                StringBuilder location = new StringBuilder();
                location.append(alphabet.get(j));
                location.append(tabs[0]);
                emptyPositions.add(location.toString());

            } else {
                StringBuilder location = new StringBuilder();
                location.append(alphabet.get(j));
                location.append(tabs[0]);
                if (labelToLocation.containsKey(tabs[j])) {
                    List value = labelToLocation.get(tabs[j]);
                    value.add(location.toString());
                    labelToLocation.put(tabs[j], value);
                }
                List lst = new ArrayList();
                lst.add(location.toString());
                labelToLocation.put(tabs[j], lst);
            }
    }
} */