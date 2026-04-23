package it.polimi.ingsw.model.characters.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;
import java.io.Serializable;

public class ShamanDTO extends CardDTO implements Serializable {
    private final int stars;

    @JsonCreator
    public ShamanDTO(@JsonProperty("stars") int stars) {
        this.stars = stars;
    }

    public  ShamanDTO(String era, int stars) {
        super(era, "Character", "Shaman");
        this.stars = stars;
    }

    public int getStars() {
        return stars;
    }
}