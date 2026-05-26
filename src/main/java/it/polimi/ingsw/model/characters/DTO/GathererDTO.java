package it.polimi.ingsw.model.characters.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.characters.Gatherer;
import java.io.Serializable;

/**
 * Holds information about a {@code Gatherer} card in DTO format.
 */
public class GathererDTO extends CardDTO implements Serializable {

    /**
     * Generates a {@code GathererDTO} object, filled with the specified parameters.
     * @param era the {@code Era} the card belongs to
     * @param id the {@code Card}'s id, used to get the corresponding texture
     */
    @JsonCreator
    public GathererDTO(@JsonProperty("era") String era, @JsonProperty("id") int id) {
        super(era, "Character", "Gatherer", id);
    }

    @Override
    public StringBuilder[] printCard() {
        StringBuilder[] lines = new StringBuilder[7];

        for (int i = 0; i < 7; i++) {
            lines[i] = new StringBuilder();
        }
        lines[0].append("+----------+");
        lines[1].append(String.format("|%-10s|", "Gatherer"));
        lines[2].append("|          |");
        lines[3].append("|          |");
        lines[4].append("|          |");
        lines[5].append(String.format("|%-10s|", getEra()));
        lines[6].append("+----------+");

        return lines;
    }

    public Gatherer fromDTO() {
        return new Gatherer(Era.valueOf(this.getEra()));
    }
}