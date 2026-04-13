package it.polimi.ingsw.model.characters.DTO;

import java.io.Serializable;

public class GathererDTO implements Serializable {
    private String era;

    public GathererDTO(String era) {
        this.era = era;
    }

    public String getEra() {
        return era;
    }
}
