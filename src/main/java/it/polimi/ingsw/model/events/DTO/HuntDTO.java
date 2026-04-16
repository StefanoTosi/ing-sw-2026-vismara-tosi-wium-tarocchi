package it.polimi.ingsw.model.events.DTO;

import it.polimi.ingsw.model.CardDTO;

import java.io.Serializable;

public class HuntDTO extends CardDTO implements Serializable {
    private final int pp;

    public HuntDTO(String era, int pp){
        super(era, "Event");
        this.pp = pp;
    }

    public int getPp() {
        return pp;
    }
}
