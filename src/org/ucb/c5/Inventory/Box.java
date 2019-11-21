package org.ucb.c5.Inventory;

import java.util.HashMap;
import java.util.Queue;

public class Box{
    private String boxDescription;
    private String boxName;
    private Experimentalthrd boxThread;
    private String labLocation;
    private String boxTemperature;
    private Queue<Location> emptySpots;
    private HashMap<String, HashMap<Concentration, Location>> everythings_loc;

    public Box (String name, String boxThread, String description, String labLocation, String temperature, Queue<Location> emptySpots, HashMap<String, HashMap<Concentration, Location>> everythings_loc) {
        this.boxName = name;
        this.boxDescription = description;
        this.boxThread = new Experimentalthrd(boxThread);
        this.labLocation = labLocation;
        this.boxTemperature = temperature;
        this.emptySpots = emptySpots;
        this.everythings_loc = everythings_loc;
    }

    public String getBoxName() {
        return boxName;
    }
    public Experimentalthrd getBoxThread() {
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
    public boolean containsName(String name){
        return everythings_loc.containsKey(name);
    }
    public void put(String name, Concentration conc){
        Location filled = emptySpots.remove();
        HashMap newstuff = new HashMap<Concentration, Location>();
        newstuff.put(conc, filled);
        everythings_loc.put(name, newstuff);
    }
    public HashMap<Concentration, Location> get(String name){
        return everythings_loc.get(name);
    }
}