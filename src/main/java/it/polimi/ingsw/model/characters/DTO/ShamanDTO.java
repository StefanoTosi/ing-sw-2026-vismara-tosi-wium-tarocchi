package it.polimi.ingsw.model.characters.DTO;

import it.polimi.ingsw.model.CardDTO;

import java.io.Serializable;

public class ShamanDTO extends CardDTO implements Serializable {
    private final int stars;
    private final String name;

    public  ShamanDTO(String era, int stars) {
        super(era, "Character");
        this.stars = stars;
        this.name = "Shaman";
    }

    public int getStars() {
        return stars;
    }

    public String getName() {
        return name;
    }
}
