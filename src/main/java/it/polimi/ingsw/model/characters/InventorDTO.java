package it.polimi.ingsw.model.characters;

import java.io.Serializable;

public class InventorDTO implements Serializable {
    private String era;
    private String icon;

    public InventorDTO(String era, String icon) {
        this.era = era;
        this.icon = icon;
    }

    public String getEra() {
        return era;
    }

    public String getIcon() {
        return icon;
    }
}
