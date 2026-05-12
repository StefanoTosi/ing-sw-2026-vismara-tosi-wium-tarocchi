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
        event[0].append(String.format("|%-10s|"));
        event[1].append("|Cave      |");
        event[2].append("|Paintings |");
        event[3].append(String.format("|%-10s|", getMinArtist()));
        event[4].append(String.format("|%-10s|", getTopPp()));
        event[5].append(String.format("|%-10s|", getEra()));
        event[6].append("+----------+");

        return event;

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
