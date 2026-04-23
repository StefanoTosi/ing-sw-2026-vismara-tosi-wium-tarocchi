package it.polimi.ingsw.model.characters.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.CardDTO;
import java.io.Serializable;

public class BuilderDTO extends CardDTO implements Serializable {
    private final int foodDiscount;
    private final int pp;

    @JsonCreator
    public BuilderDTO(int foodDiscount, int pp, String era) {
        this.foodDiscount = foodDiscount;
        this.pp = pp;
        super(era, "Character", "Builder");
    }

    public int getFoodDiscount() {
        return foodDiscount;
    }

    public int getPp() {
        return pp;
    }
}