package it.polimi.ingsw.model.characters.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.CardDTO;
import java.io.Serializable;

public class GathererDTO extends CardDTO implements Serializable {
    @JsonCreator
    public GathererDTO(String era) {
        super(era, "Character", "Gatherer");
    }
}