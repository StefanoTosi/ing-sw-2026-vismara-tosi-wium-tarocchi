package it.polimi.ingsw.model.characters.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;
import java.io.Serializable;
import java.util.List;

public class ShamanDTO extends CardDTO implements Serializable {
    private final int stars;

    public ShamanDTO(@JsonProperty("era") String era, @JsonProperty("stars") int stars, @JsonProperty("id") int id) {
        super(era, "Character", "Shaman", id);
        this.stars = stars;
    }
    /**
     * Function to print the Shaman cards of a player
     */
    @Override
    public StringBuilder[] printCard() {
        StringBuilder[] lines = new StringBuilder[7];

        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder();
        }
            lines[0].append("+----------+");
            lines[1].append(String.format("|%-10s|", "Shaman"));
            lines[2].append(String.format("|Stars: %d |", getStars()));
            lines[3].append("|          |");
            lines[4].append("|          |");
            lines[5].append(String.format("|%-10s|", getEra()));
            lines[6].append("+----------+");

        return lines;
    }
    @Override
    public int getStars() {
        return stars;
    }
}