package it.polimi.ingsw.model;

import java.io.Serializable;

public class CardDTO implements Serializable {
    private final String era;
    private final String name;
    private final String TYPE;

    public CardDTO(String era,  String name, String TYPE) {
        this.era = era;
        this.name = name;
        this.TYPE = TYPE;
    }

    public String getEra() {
        return era;
    }

    public String getName() {
        return name;
    }

    public String getType(){return TYPE;}
}
