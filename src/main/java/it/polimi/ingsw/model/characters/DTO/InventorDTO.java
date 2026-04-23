package it.polimi.ingsw.model.characters.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.CardDTO;
import java.io.Serializable;

public class InventorDTO extends CardDTO implements Serializable {
    private final String icon;

    @JsonCreator
    public InventorDTO(String era, String icon) {
        super(era, "Character", "Inventor");
        this.icon = icon;
    }

    public String getInventionIcon() {
        return icon;
    }
}