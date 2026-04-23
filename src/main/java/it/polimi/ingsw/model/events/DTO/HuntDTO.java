package it.polimi.ingsw.model.events.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;

import java.io.Serializable;

public class HuntDTO extends CardDTO implements Serializable {
    private final int pp;

    @JsonCreator
    public HuntDTO(@JsonProperty("era") String era, @JsonProperty("pp") int pp){
        super(era, "Event", "Hunt");
        this.pp = pp;
    }

    public int getPp() {
        return pp;
    }
}