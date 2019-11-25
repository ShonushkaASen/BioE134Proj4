package org.ucb.c5.Inventory;

import java.util.HashMap;
import java.util.Queue;

public class Box{
    private String boxDescription;
    private String boxName;
    private String boxThread;
    private String labLocation;
    private String boxTemperature;
    private Queue<Location> emptySpots;
    private HashMap<String, HashMap<Double, Location>> everythings_loc;
    private HashMap[][] boxGrid;

    public Box (String name, String boxThread, String description, String labLocation, String temperature, Queue<Location> emptySpots, HashMap<String, HashMap<Double, Location>> everythings_loc, HashMap[][] boxGrid) {
        this.boxName = name;
        this.boxDescription = description;
        this.boxThread = boxThread;
        this.labLocation = labLocation;
        this.boxTemperature = temperature;
        this.emptySpots = emptySpots;
        this.everythings_loc = everythings_loc;
        this.boxGrid = boxGrid;
    }

    public String getBoxName() {
        return boxName;
    }
    public String getBoxThread() {
        return boxThread;
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
    public HashMap[][] getBoxGrid() {
        return boxGrid;
    }
    public boolean containsName(String name){
        return everythings_loc.containsKey(name);
    }
    public void put(String name, Double conc){
        Location filled = emptySpots.remove();
        HashMap newstuff = new HashMap<Double, Location>();
        newstuff.put(conc, filled);
        everythings_loc.put(name, newstuff);
    }
    public HashMap<Double, Location> get(String name){
        return everythings_loc.get(name);
    }
}