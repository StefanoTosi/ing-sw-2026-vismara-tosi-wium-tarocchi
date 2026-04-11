package it.polimi.ingsw.model.characters;

import java.io.Serializable;

public class HunterDTO implements Serializable {
    private boolean icon;
    private String era;

    public HunterDTO(boolean icon, String era){
        this.icon = icon;
        this.era = era;
    }

    public boolean getIcon() {
        return icon;
    }

    public String getEra() {
        return era;
    }
}
