package it.polimi.ingsw.model.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.PlayerDTO;

import java.io.Serializable;

/**
 * Contains information about the effect a certain event has had on a certain player
 */
public class EventResult implements Serializable {
    private final String event;
    private final PlayerDTO player;
    private final int deltaPp;
    private final int deltaFood;

    @JsonCreator
    public EventResult(@JsonProperty("event") String event, @JsonProperty("player")  PlayerDTO player, @JsonProperty("deltaPp") int deltaPp, @JsonProperty("deltaFood") int deltaFood) {
        this.event = event;
        this.player = player;
        this.deltaPp = deltaPp;
        this.deltaFood = deltaFood;
    }

    public String getEvent() {
        return event;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public int getDeltaPp() {
        return deltaPp;
    }

    public int getDeltaFood() {
        return deltaFood;
    }
}
