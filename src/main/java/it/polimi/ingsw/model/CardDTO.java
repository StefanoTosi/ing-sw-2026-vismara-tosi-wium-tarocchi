package it.polimi.ingsw.model;

import java.io.Serializable;

public class CardDTO implements Serializable {
    private final String era;
    private final String TYPE;

    public CardDTO(String era, String type) {
        this.era = era;
        this.TYPE = type;
    }

    public String getEra() {
        return era;
    }

    public String getType() {
        return TYPE;
    }

    public String getName() {
        return "";
    }

    public int getCost() {
        return 0;
    }

    public int getPp() {
        return 0;
    }

    public int getEffectPp() {
        return 0;
    }

    public String getEffect() {
        return "";
    }

    public int getFoodDiscount() {
        return 0;
    }

    public boolean getIcon() {
        return false;
    }

    public String getInventionIcon() {
        return "";
    }

    public int getStars() {
        return 0;
    }
}
