package it.polimi.ingsw;

/**
 * Represent all the common elements of the characters.
 */
public abstract class Character implements Card {
    protected String name;
    protected String TYPE="Character";

    @Override
    public String getType() {
        return TYPE;
    }

    public String toString() {
        return "name: " + name + "\ntype: " + TYPE;
    }
}
