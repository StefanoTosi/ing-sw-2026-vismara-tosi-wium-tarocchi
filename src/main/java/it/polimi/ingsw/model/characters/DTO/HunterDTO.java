package it.polimi.ingsw.model.characters.DTO;

import it.polimi.ingsw.model.CardDTO;

import java.io.Serializable;

public class HunterDTO extends CardDTO implements Serializable {
    private final boolean icon;
    private final String name;

    public HunterDTO(boolean icon, String era){
        this.icon = icon;
        super(era, "Character");
        this.name = "Hunter";
    }

    public boolean getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }
}
