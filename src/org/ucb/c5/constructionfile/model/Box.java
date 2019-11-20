package org.ucb.c5.constructionfile.model;

import java.util.HashMap;
import java.util.Queue;

public class Box {
    private String boxDescription;
    private String boxName;
    private String labLocation;
    private String boxTemperature;
    private Queue<Location> emptySpots;
    private HashMap nameToConcToLoc;

    public Box (String name, String description, String labLocation, String temperature, Queue<Location> emptySpots, HashMap nameToConcToLoc) {
        this.boxName = name;
        this.boxDescription = description;
        this.labLocation = labLocation;
        this.boxTemperature = temperature;
        this.emptySpots = emptySpots;
        this.nameToConcToLoc = nameToConcToLoc;
    }

    public String getBoxName() {
        return boxName;
    }
    public String getBoxDescription() {
        return boxDescription;
    }
    public String getLabLocation() {
        return labLocation;
    }
    public String getBoxTemperature() {
        return boxTemperature;
    }
    public Queue getEmptySpots() {
        return emptySpots;
    }
    public HashMap getNameToConcToLoc() {
        return nameToConcToLoc;
    }
}
