package it.polimi.ingsw.model.characters.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;
import java.io.Serializable;

public class ArtistDTO extends CardDTO implements Serializable {
    @JsonCreator
    public ArtistDTO(@JsonProperty("era") String era, @JsonProperty("id") int id) {
        super(era, "Character", "Artist", id);
    }
}