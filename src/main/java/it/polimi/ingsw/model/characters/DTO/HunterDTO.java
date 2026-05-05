package it.polimi.ingsw.model.characters.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;
import java.io.Serializable;

public class HunterDTO extends CardDTO implements Serializable {
    private final boolean icon;

    @JsonCreator
    public HunterDTO(@JsonProperty("icon") boolean icon, @JsonProperty("era") String era, @JsonProperty("id") int id){
        this.icon = icon;
        super(era, "Character", "Hunter", id);
    }

    public boolean getIcon() {
        return icon;
    }
}