package it.polimi.ingsw.model.characters.DTO;

import it.polimi.ingsw.model.CardDTO;

import java.io.Serializable;

public class GathererDTO extends CardDTO implements Serializable {
    private final String name;

    public GathererDTO(String era) {
        super(era, "Character");
        this.name = "Gatherer";
    }

    public String getName() {
        return name;
    }
}
