package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;

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

    public void applyEffect(List<Player> players){}

    // TODO: questa funzione dovrebbe lanciare un'eccezione
    @Override
    public void addToPlayer(Player player) {
        return;
    }
}
