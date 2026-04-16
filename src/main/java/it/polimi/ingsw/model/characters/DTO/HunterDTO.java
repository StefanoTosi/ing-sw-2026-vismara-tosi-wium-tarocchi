package it.polimi.ingsw.model.characters.DTO;

import it.polimi.ingsw.model.CardDTO;
import java.io.Serializable;

public class HunterDTO extends CardDTO implements Serializable {
    private final boolean icon;

    public HunterDTO(boolean icon, String era){
        this.icon = icon;
        super(era, "Character", "Hunter");
    }

    public boolean getIcon() {
        return icon;
    }
}