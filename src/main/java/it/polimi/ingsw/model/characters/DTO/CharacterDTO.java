package it.polimi.ingsw.model.characters.DTO;

import java.io.Serializable;

public class CharacterDTO implements Serializable {
    private String era;

    public CharacterDTO(String era){
        this.era = era;
    }

    public String getEra() {
        return era;
    }
}
