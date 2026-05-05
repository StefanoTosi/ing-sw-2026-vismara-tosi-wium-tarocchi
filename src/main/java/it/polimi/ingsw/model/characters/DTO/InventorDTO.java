package it.polimi.ingsw.model.characters.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;
import java.io.Serializable;

public class InventorDTO extends CardDTO implements Serializable {
    private final String icon;

    @JsonCreator
    public InventorDTO(@JsonProperty("era") String era, @JsonProperty("icon") String icon, @JsonProperty("id") int id) {
        super(era, "Character", "Inventor", id);
        this.icon = icon;
    }

    public String getInventionIcon() {
        return icon;
    }
}