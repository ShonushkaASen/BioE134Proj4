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
<<<<<<< HEAD
    private HashMap<String, HashMap<Double, Location>> everythings_loc;
    private HashMap[][] boxGrid;

    public Box (String name, String boxThread, String description, String labLocation, String temperature, Queue<Location> emptySpots, HashMap<String, HashMap<Double, Location>> everythings_loc, HashMap[][] boxGrid) {
=======
    private HashMap<String, HashMap<Double, Location>> mapOfNameToMapOfConcentrationToLocation;

    public Box (String name, String boxThread, String description, String labLocation, String temperature, Queue<Location> emptySpots, HashMap<String, HashMap<Double, Location>> nameToConcentrationToLocation) {
>>>>>>> 66e517edf5d1094e028cf5e563cb72825e7f98e6
        this.boxName = name;
        this.boxDescription = description;
        this.boxThread = boxThread;
        this.labLocation = labLocation;
        this.boxTemperature = temperature;
        this.emptySpots = emptySpots;
<<<<<<< HEAD
        this.everythings_loc = everythings_loc;
        this.boxGrid = boxGrid;
=======
        this.mapOfNameToMapOfConcentrationToLocation = nameToConcentrationToLocation;
>>>>>>> 66e517edf5d1094e028cf5e563cb72825e7f98e6
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
        return mapOfNameToMapOfConcentrationToLocation.containsKey(name);
    }
    public void put(String name, Double conc){
        Location filled = emptySpots.remove();
        HashMap concentrationAndLocation = new HashMap<Double, Location>();
        concentrationAndLocation.put(conc, filled);
        mapOfNameToMapOfConcentrationToLocation.put(name, concentrationAndLocation);
    }
    public HashMap<Double, Location> get(String name){
        return mapOfNameToMapOfConcentrationToLocation.get(name);
    }
}