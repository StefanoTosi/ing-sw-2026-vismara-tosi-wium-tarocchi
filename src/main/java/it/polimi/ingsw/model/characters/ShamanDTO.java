package it.polimi.ingsw.model.characters;

import java.io.Serializable;

public class ShamanDTO implements Serializable {
    private String era;
    private int stars;

    public  ShamanDTO(String era, int stars) {
        this.era = era;
        this.stars = stars;
    }

    public String getEra() {
        return era;
    }

    public int getStars() {
        return stars;
    }
}
