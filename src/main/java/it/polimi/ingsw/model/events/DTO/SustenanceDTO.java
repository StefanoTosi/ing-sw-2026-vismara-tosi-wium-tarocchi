package it.polimi.ingsw.model.events.DTO;

import it.polimi.ingsw.model.CardDTO;

import java.io.Serializable;

public class SustenanceDTO extends CardDTO implements Serializable {
    private final int pp;

    public SustenanceDTO(String era, int pp){
        super(era, "Event", "Sustenance");
        this.pp = pp;
    }

    public int getPp() {
        return pp;
    }
}
