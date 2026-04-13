package it.polimi.ingsw.model;

public class CardDTO {
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
