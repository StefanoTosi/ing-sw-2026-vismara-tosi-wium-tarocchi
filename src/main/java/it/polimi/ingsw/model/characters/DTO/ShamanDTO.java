package it.polimi.ingsw.model.characters.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;
import java.io.Serializable;

public class ShamanDTO extends CardDTO implements Serializable {
    private final int stars;

    public ShamanDTO(@JsonProperty("era") String era, @JsonProperty("stars") int stars, @JsonProperty("id") int id) {
        super(era, "Character", "Shaman", id);
        this.stars = stars;
    }

    public int getStars() {
        return stars;
    }
}