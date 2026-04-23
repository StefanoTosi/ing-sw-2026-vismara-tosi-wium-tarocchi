package it.polimi.ingsw.model.events.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.CardDTO;

import java.io.Serializable;

public class SustenanceDTO extends CardDTO implements Serializable {
    private final int pp;

    @JsonCreator
    public SustenanceDTO() {
        this.pp = 0;
    }

    public SustenanceDTO(String era, int pp){
        super(era, "Event", "Sustenance");
        this.pp = pp;
    }

    public int getPp() {
        return pp;
    }
}
