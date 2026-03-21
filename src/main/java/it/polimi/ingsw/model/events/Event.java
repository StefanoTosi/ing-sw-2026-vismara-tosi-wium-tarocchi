package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;

public abstract class Event implements Card {
    protected String TYPE = "Event";
    protected String name;
    protected Era era;

    @Override
    public String getType (){
        return TYPE;
    }

    public String toString() {
        return "name: " + name + "\ntype: " + TYPE;
    }

    // TODO: questa funzione dovrebbe lanciare un'eccezione
    @Override
    public void addToPlayer(Player player) {
        return;
    }
}
