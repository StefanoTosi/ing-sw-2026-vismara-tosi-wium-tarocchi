package it.polimi.ingsw.model;

import java.io.Serializable;

public class CardDTO implements Serializable {
    private final String era;
    private final String name;

    public CardDTO(String era,  String name) {
        this.era = era;
        this.name = name;
    }

    public String getEra() {
        return era;
    }

    public String getName() {
        return name;
    }
}
