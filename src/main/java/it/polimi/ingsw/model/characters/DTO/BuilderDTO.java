package it.polimi.ingsw.model.characters.DTO;

import it.polimi.ingsw.model.CardDTO;

import java.io.Serializable;

public class BuilderDTO extends CardDTO implements Serializable {
    private final int foodDiscount;
    private final int pp;
    private final String name;

    public BuilderDTO(int foodDiscount, int pp, String era) {
        this.foodDiscount = foodDiscount;
        this.pp = pp;
        this.name = "Builder";
        super(era, "Character");
    }

    public int getFoodDiscount() {
        return foodDiscount;
    }

    public int getPp() {
        return pp;
    }

    public String getName() {
        return name;
    }
}
