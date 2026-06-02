package it.polimi.ingsw.model.events.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.events.ShamanicRitual;

import java.io.Serializable;

/**
 * Holds information about a {@code ShamanicRitual} card in DTO format.
 */
public class ShamanicRitualDTO extends CardDTO implements Serializable {
    private final int winnerPp;
    private final int loserPp;

    /**
     * Generates a {@code ShamanicRitualDTO} object, filled with the specified parameters.
     * @param winnerPp
     * @param loserPp
     * @param era the {@code Era} the card belongs to
     * @param id the {@code Card}'s id, used to get the corresponding texture
     */
    @JsonCreator
    public ShamanicRitualDTO (@JsonProperty("winnerPp") int winnerPp, @JsonProperty("loserPp") int loserPp, @JsonProperty("era") String era, @JsonProperty("id") int id){
        super(era, "Event",  "ShamanicRitual", id);
        this.winnerPp = winnerPp;
        this.loserPp = loserPp;
    }

    @Override
    public StringBuilder[] printCard(){
        StringBuilder[] event = new StringBuilder[7];
        for(int i = 0; i < 7; i++){
            event[i] = new StringBuilder();
        }
        event[0].append("|%-10s|");
        event[1].append("|Shamanic  |");
        event[2].append("|Ritual    |");
        event[3].append(String.format("|%-10s|", getWinnerPp()));
        event[4].append(String.format("|%-10s|", getLoserPp()));
        event[5].append(String.format("|%-10s|", getEra()));
        event[6].append("+----------+");

        return event;

    }

    public int getWinnerPp() {
        return winnerPp;
    }

    public int getLoserPp() {
        return loserPp;
    }

    public ShamanicRitual fromDTO() {
        return new ShamanicRitual(this.winnerPp, this.loserPp, Era.valueOf(this.getEra()), getId());
    }
}
