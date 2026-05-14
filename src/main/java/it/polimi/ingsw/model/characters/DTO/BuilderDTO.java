package it.polimi.ingsw.model.characters.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.characters.Builder;

import java.io.Serializable;
import java.util.List;

public class BuilderDTO extends CardDTO implements Serializable {
    private final int foodDiscount;
    private final int pp;

    @JsonCreator
    public BuilderDTO(@JsonProperty("foodDiscount") int foodDiscount, @JsonProperty("pp") int pp, @JsonProperty("era") String era, @JsonProperty("id") int id) {
        this.foodDiscount = foodDiscount;
        this.pp = pp;
        super(era, "Character", "Builder", id);
    }

    /**
     * Function to print the Inventor cards of a player
    */
    @Override
    public StringBuilder[] printCard() {
        StringBuilder[] lines = new StringBuilder[7];

        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder();
        }

        lines[0].append("+----------+");
        lines[1].append(String.format("|%-10s|", "Builder"));
        lines[2].append(String.format("|discount %d|", getFoodDiscount()));
        lines[3].append("|          |");
        lines[4].append("|          |");
        lines[5].append(String.format("|%-10s|", getEra()));
        lines[6].append("+----------+");

        return lines;
    }

    @Override
    public int getFoodDiscount() {
        return foodDiscount;
    }

    @Override
    public int getPp() {
        return pp;
    }

    public Builder fromDTO() {
        return new Builder(this.foodDiscount, this.pp, Era.valueOf(this.getEra()));
    }
}