package it.polimi.ingsw.model.characters.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;
import java.io.Serializable;

public class GathererDTO extends CardDTO implements Serializable {
    @JsonCreator
    public GathererDTO(@JsonProperty("era") String era, @JsonProperty("id") int id) {
        super(era, "Character", "Gatherer", id);
    }
}