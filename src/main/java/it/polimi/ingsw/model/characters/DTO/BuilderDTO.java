package it.polimi.ingsw.model.characters.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;
import java.io.Serializable;

public class BuilderDTO extends CardDTO implements Serializable {
    private final int foodDiscount;
    private final int pp;

    @JsonCreator
    public BuilderDTO(@JsonProperty("foodDiscount") int foodDiscount, @JsonProperty("pp") int pp, @JsonProperty("era") String era) {
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