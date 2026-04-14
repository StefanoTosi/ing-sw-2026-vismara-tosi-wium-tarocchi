package it.polimi.ingsw.model.events.DTO;

import java.io.Serializable;

public class SustenanceDTO implements Serializable {
    private String era;
    private int pp;

    public SustenanceDTO(String era, int pp){
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
