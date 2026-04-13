package it.polimi.ingsw.model.characters.DTO;

import java.io.Serializable;

public class BuilderDTO implements Serializable {
    private int foodDiscount;
    private int pp;
    private String era;

    public BuilderDTO(int foodDiscount, int pp, String era) {
        this.foodDiscount = foodDiscount;
        this.pp = pp;
        this.era = era;
    }

    public int getFoodDiscount() {
        return foodDiscount;
    }

    public int getPp() {
        return pp;
    }

    public String getEra() {
        return era;
    }

}
