package it.polimi.ingsw.model.events.DTO;

import it.polimi.ingsw.model.CardDTO;
import java.io.Serializable;

public class CavePaintingsDTO extends CardDTO implements Serializable {
    private final int topPp;
    private final int bottomPp;
    private final int minArtist;

    public CavePaintingsDTO(String era, int minArtist, int topPp, int bottomPp){
        super(era, "Event");
        this.minArtist = minArtist;
        this.topPp = topPp;
        this.bottomPp = bottomPp;
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
