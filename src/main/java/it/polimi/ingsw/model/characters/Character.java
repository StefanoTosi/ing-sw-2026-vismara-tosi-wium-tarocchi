package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Era;

/**
 * Represent all the common elements of the characters.
 */
public abstract class Character extends Card {
    protected String name;

    public Character(Era era) {
        super(era);
        this.TYPE="Character";
    }

    @Override
    public String getName() {
        return name;
    }

    public String toString() {
        return "name: " + name + "\ntype: " + TYPE;
    }
}
