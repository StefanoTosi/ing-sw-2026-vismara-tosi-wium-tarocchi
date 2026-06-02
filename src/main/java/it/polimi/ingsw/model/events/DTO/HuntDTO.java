package it.polimi.ingsw.model.events.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.events.Hunt;

import java.io.Serializable;

/**
 * Holds information about a {@code Hunt} card in DTO format.
 */
public class HuntDTO extends CardDTO implements Serializable {
    private final int pp;

    /**
     * Generates a {@code HuntDTO} object, filled with the specified parameters.
     * @param era the {@code Era} the card belongs to
     * @param pp the amount of prestige points awarded for each {@code Hunter}
     * @param id the {@code Card}'s id, used to get the corresponding texture
     */
    @JsonCreator
    public HuntDTO(@JsonProperty("era") String era, @JsonProperty("pp") int pp, @JsonProperty("id") int id) {
        super(era, "Event", "Hunt", id);
        this.pp = pp;
    }

    @Override
    public StringBuilder[] printCard(){
        StringBuilder[] event = new StringBuilder[7];
        for(int i = 0; i < 7; i++){
            event[i] = new StringBuilder();
        }
        event[0].append("|%-10s|");
        event[1].append(String.format("|%-10s|", getName()));
        event[2].append(String.format("|%-10s|", getPp()));
        event[3].append("|          |");
        event[4].append("|          |");
        event[5].append(String.format("|%-10s|", getEra()));
        event[6].append("+----------+");

        return event;

    }

    public int getPp() {
        return pp;
    }

    public Hunt fromDTO() {
        return new Hunt(this.pp, Era.valueOf(this.getEra()), getId());
    }
}