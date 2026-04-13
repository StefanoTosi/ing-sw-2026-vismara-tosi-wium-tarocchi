package it.polimi.ingsw.model.characters.DTO;

import java.io.Serializable;

public class ArtistDTO implements Serializable {
    private String era;

    public ArtistDTO(String era) {
        this.era = era;
    }

    public String getEra() {
        return era;
    }
}
