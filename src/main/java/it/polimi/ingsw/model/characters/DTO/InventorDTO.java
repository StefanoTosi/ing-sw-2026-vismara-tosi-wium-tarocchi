package it.polimi.ingsw.model.characters.DTO;

import it.polimi.ingsw.model.CardDTO;

import java.io.Serializable;

public class InventorDTO extends CardDTO implements Serializable {
    private final String icon;
    private final String name;

    public InventorDTO(String era, String icon) {
        super(era, "Character");
        this.icon = icon;
        this.name = "Inventor";
    }

    public String getInventionIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }
}
