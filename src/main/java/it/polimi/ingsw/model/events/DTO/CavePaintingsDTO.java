package it.polimi.ingsw.model.events.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.events.CavePaintings;

import java.io.Serializable;

/**
 * Holds information about a {@code CavePaintings} card in DTO format.
 */
public class CavePaintingsDTO extends CardDTO implements Serializable {
    private final int topPp;
    private final int bottomPp;
    private final int minArtist;

    /**
     * Generates a {@code CavePaintingsDTO} object, filled with the specified parameters.
     * @param era the {@code Era} the card belongs to
     * @param minArtist the minimum number of {@code Artists} players are required in order to win pps
     * @param topPp the amount of prestige points players are deducted if they do not own enough {@code Artists}. This parameter is required to be positive.
     * @param bottomPp the amount of prestige point awarded to players who own enough {@code Artists}
     * @param id the {@code Card}'s id, used to get the corresponding texture
     */
    @JsonCreator
    public CavePaintingsDTO(@JsonProperty("era") String era, @JsonProperty("minArtist") int minArtist, @JsonProperty("topPp") int topPp, @JsonProperty("bottomPp") int bottomPp, @JsonProperty("id") int id){
        super(era, "Event", "CavePaintings", id);
        this.minArtist = minArtist;
        this.topPp = topPp;
        this.bottomPp = bottomPp;
    }

    @Override
    public StringBuilder[] printCard(){
        StringBuilder[] event = new StringBuilder[7];
        for(int i = 0; i < 7; i++){
            event[i] = new StringBuilder();
        }
        event[0].append("|%-10s|");
        event[1].append("|Cave      |");
        event[2].append("|Paintings |");
        event[3].append(String.format("|%-10s|", getMinArtist()));
        event[4].append(String.format("|%-10s|", getTopPp()));
        event[5].append(String.format("|%-10s|", getEra()));
        event[6].append("+----------+");

        return event;

    }

    public CavePaintings fromDTO() {
        return new CavePaintings(this.minArtist, Era.valueOf(this.getEra()), this.topPp, this.bottomPp);
    }

    public int getMinArtist() {
        return minArtist;
    }

    public int getTopPp() {
        return topPp;
    }

    public int getBottomPp() {
        return bottomPp;
    }
}