package it.polimi.ingsw.model.events.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;
import java.io.Serializable;

public class CavePaintingsDTO extends CardDTO implements Serializable {
    private final int topPp;
    private final int bottomPp;
    private final int minArtist;

    @JsonCreator
    public CavePaintingsDTO(@JsonProperty("era") String era, @JsonProperty("minArtist") int minArtist, @JsonProperty("topPp") int topPp, @JsonProperty("bottomPp") int bottomPp){
        super(era, "Event", "Cave Paintings");
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
