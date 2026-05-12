package it.polimi.ingsw.model.events.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.CardDTO;

import java.io.Serializable;

public class SustenanceDTO extends CardDTO implements Serializable {
    private final int pp;

    @JsonCreator
    public SustenanceDTO(@JsonProperty("era") String era, @JsonProperty("pp") int pp, @JsonProperty("id") int id){
        super(era, "Event", "Sustenance", id);
        this.pp = pp;
    }

    @Override
    public StringBuilder[] printCard(){
        StringBuilder[] event = new StringBuilder[7];
        for(int i = 0; i < 7; i++){
            event[i] = new StringBuilder();
        }
        event[0].append(String.format("|%-10s|"));
        event[1].append(String.format("|%-10s|", getName()));
        event[2].append("|          |");
        event[3].append("|          |");
        event[4].append("|          |");
        event[5].append(String.format("|%-10s|", getEra()));
        event[6].append("+----------+");

        return event;

    }

    public int getPp() {
        return pp;
    }
}
