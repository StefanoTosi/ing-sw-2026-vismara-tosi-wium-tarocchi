package it.polimi.ingsw.model.events.DTO;

import java.io.Serializable;

public class CavePaintingsDTO implements Serializable {
    private String era;
    private int topPp;
    private int bottomPp;
    private int minArtist;

    public CavePaintingsDTO(String era, int minArtist, int topPp, int bottomPp){
        this.era  = era;
        this.minArtist = minArtist;
        this.topPp = topPp;
        this.bottomPp = bottomPp;
    }

    public String getEra() {
        return era;
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
