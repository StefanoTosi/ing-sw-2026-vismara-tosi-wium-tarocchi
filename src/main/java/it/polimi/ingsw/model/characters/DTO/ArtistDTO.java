package it.polimi.ingsw.model.characters.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.characters.Artist;
import java.io.Serializable;

/**
 * Holds information about an {@code Artist} card in DTO format.
 */
public class ArtistDTO extends CardDTO implements Serializable {

    /**
     * Generates an {@code ArtistDTO} object, filled with the specified parameters.
     * @param era the {@code Era} the card belongs to
     * @param id the {@code Card}'s id, used to get the corresponding texture
     */
    @JsonCreator
    public ArtistDTO(@JsonProperty("era") String era, @JsonProperty("id") int id) {
        super(era, "Character", "Artist", id);
    }

    @Override
    public StringBuilder[] printCard() {
        StringBuilder[] lines = new StringBuilder[7];

        for(int i=0; i < 7; i++){
            lines[i] = new StringBuilder();
        }

        lines[0].append("+----------+");
        lines[1].append(String.format("|%-10s|", "Artist"));
        lines[2].append("|          |");
        lines[3].append("|          |");
        lines[4].append("|          |");
        lines[5].append(String.format("|%-10s|", getEra()));
        lines[6].append("+----------+");

        return lines;
    }

    public Artist fromDTO() {
        return new Artist(Era.valueOf(this.getEra()));
    }
}