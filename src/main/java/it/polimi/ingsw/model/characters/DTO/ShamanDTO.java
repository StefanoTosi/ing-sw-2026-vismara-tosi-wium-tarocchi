package it.polimi.ingsw.model.characters.DTO;

import it.polimi.ingsw.model.CardDTO;
import java.io.Serializable;

public class ShamanDTO extends CardDTO implements Serializable {
    private final int stars;

    public  ShamanDTO(String era, int stars) {
        super(era, "Character", "Shaman");
        this.stars = stars;
    }

    public int getStars() {
        return stars;
    }
}