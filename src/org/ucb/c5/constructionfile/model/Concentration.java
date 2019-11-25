package org.ucb.c5.constructionfile.model;

public class Concentration {
    private final String molarity;
    private final double concentration;
    public Concentration(Double concentration) {
        this.concentration = concentration;
        this.molarity = "uM";
    }
    public Concentration() {
        this.concentration = -1;
        this.molarity = "uM";
    }

    public double getConcentration() {
        return concentration;
    }

    public String getMolarity() {
        return molarity;
    }
}
