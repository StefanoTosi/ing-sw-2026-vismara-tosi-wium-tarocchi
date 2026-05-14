package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import java.util.List;

public abstract class Event extends Card {
    protected String name;

    public Event(Era era) {
        super(era);
        this.TYPE = "Event";
    }

    public String toString() {
        return "name: " + name + "\ntype: " + TYPE;
    }

    public List<EventResult> applyEffect(List<Player> players) {
        return null;
    }

    @Override
    public void addToPlayer(Player player) throws IllegalActionException {
        throw new IllegalActionException("Player " + player + " tried to draw an event");
    }
}
