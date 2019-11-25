package org.ucb.c5.Inventory;

public class Location {
    private String row;
    private String col;
    public Location(int row, int col){
        this.row = Integer.toString(row);
        this.col = Integer.toString(col);
    }
    public String getRow(){
        return this.row;
    }
    public String getCol(){
        return this.col;
    }
}
