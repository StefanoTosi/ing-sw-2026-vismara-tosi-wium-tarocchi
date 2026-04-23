package it.polimi.ingsw.model.events.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;
import it.polimi.ingsw.model.Era;

import java.io.Serializable;

public class ShamanicRitualDTO extends CardDTO implements Serializable {
    private final int winnerPp;
    private final int loserPp;

    @JsonCreator
    public ShamanicRitualDTO (@JsonProperty("winnerPp") int winnerPp, @JsonProperty("loserPp") int loserPp, @JsonProperty("era") String era){
        super(era, "Event",  "Shamanic Ritual");
        this.winnerPp = winnerPp;
        this.loserPp = loserPp;
    }

    public int getWinnerPp() {
        return winnerPp;
    }

    public int getLoserPp() {
        return loserPp;
    }
}
