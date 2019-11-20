package org.andersonlab.pipetteaid.model;

import java.util.HashMap;
import java.util.Map;
import java.util.AbstractMap.*;

/**
 *
 * @author J. Christopher Anderson
 */
public class Deck {

    private final Plate[][] plates;
    private final Map<String, SimpleEntry<Integer,Integer>> nameToPos;
    
    public Deck() {
        DeckConfig config = DeckConfig.getInstance();
        plates = new Plate[config.num_rows][config.num_cols];
        nameToPos = new HashMap<>();
    }
    
    public void addPlate(String plateName, int row, int col) throws Exception {
        if(plates[row][col] != null) {
            throw new Exception();
        }
        plates[row][col] = new Plate(plateName, PCRPlateConfig.getInstance());
        nameToPos.put(plateName, new SimpleEntry(row, col));
    }
    
    public void removePlate(int row, int col) {
        plates[row][col] = null;
    }
    
    public void removePlate(String plateName) {
        SimpleEntry<Integer,Integer> pos = nameToPos.get(plateName);
        removePlate(pos.getKey(), pos.getValue());
    }
    
    public Plate getPlate(int row, int col) {
        return plates[row][col];
    }

    public SimpleEntry<Integer, Integer> getPlatePos(String location) {
        String name = location;
        if(name.contains("/")) {
            String[] splitted = name.split("/");
            name = splitted[0];
        }
        return nameToPos.get(name);
    }

    /**
     * Heper method for location in the form of plate_name/A2
     * 
     * @param location plate_name/A2
     * @return the Well object in its Plate
     */
    public Well getWell(String location) throws Exception {
        String[] slitted = location.split("/");
        String plateName = slitted[0];
        SimpleEntry<Integer,Integer> deckrowcol = nameToPos.get(plateName);
        Plate aplate = plates[deckrowcol.getKey()][deckrowcol.getValue()];
        
        SimpleEntry<Integer,Integer> A1 = Well.parseWellLabel(slitted[1]);
        return aplate.getWell(A1.getKey(), A1.getValue());
    }
    
    public static SimpleEntry<Integer, Integer> calcDeckPosition(String location) throws Exception {
        String A1 = location;
        if(A1.contains("/")) {
            String[] splitted = A1.split("/");
            A1 = splitted[1];
        }
        return Well.parseWellLabel(A1);
    }
}
