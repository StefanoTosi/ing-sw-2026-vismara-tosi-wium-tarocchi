package it.polimi.ingsw.model.events.DTO;

import it.polimi.ingsw.model.CardDTO;
import it.polimi.ingsw.model.Era;

import java.io.Serializable;

public class ShamanicRitualDTO extends CardDTO implements Serializable {
    private final int winnerPp;
    private final int loserPp;


    public ShamanicRitualDTO (int winnerPp, int loserPp, String era){
        super(era, "Event");
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
