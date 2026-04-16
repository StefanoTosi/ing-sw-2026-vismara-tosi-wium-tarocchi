package it.polimi.ingsw.model.characters.DTO;

import it.polimi.ingsw.model.CardDTO;
import java.io.Serializable;

public class InventorDTO extends CardDTO implements Serializable {
    private final String icon;

    public InventorDTO(String era, String icon) {
        super(era, "Character", "Inventor");
        this.icon = icon;
    }

    public String getInventionIcon() {
        return icon;
    }
}