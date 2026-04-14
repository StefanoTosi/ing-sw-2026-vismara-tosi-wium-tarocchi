package it.polimi.ingsw.model.events.DTO;

import java.io.Serializable;

public class HuntDTO implements Serializable {
    private String era;
    private int pp;

    public HuntDTO(String era, int pp){
        this.era = era;
        this.pp = pp;
    }

    public String getEra() {
        return era;
    }

    public int getPp() {
        return pp;
    }
}
