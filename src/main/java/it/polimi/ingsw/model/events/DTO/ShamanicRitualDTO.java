package it.polimi.ingsw.model.events.DTO;

import it.polimi.ingsw.model.Era;

import java.io.Serializable;

public class ShamanicRitualDTO implements Serializable {
    private String era;
    private int winnerPp;
    private int loserPp;


    public ShamanicRitualDTO (int winnerPp, int loserPp, String era){
        this.era = era;
        this.winnerPp = winnerPp;
        this.loserPp = loserPp;
    }

    public String getEra() {
        return era;
    }

    public int getWinnerPp() {
        return winnerPp;
    }

    public int getLoserPp() {
        return loserPp;
    }
}
