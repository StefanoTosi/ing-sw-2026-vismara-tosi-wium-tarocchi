package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Era;

/**
 * Represents common elements of all {@code Characters}.<br>
 * Each character can be an {@code Artist}, a {@code Builder}, a {@code Gatherer}, a {@code Hunter}, an {@code Inventor} or a {@code Shaman}.
 */
public abstract class Character extends Card {
    protected String name;

    /**
     * Generates a {@code Character} card belonging to the specified era.
     * @param era the era the card belongs to
     */
    public Character(Era era) {
        super(era);
        this.TYPE = "Character";
    }

    /**
     * Generates a {@code Character} card, whose subtype is known and identified by its name, belonging to the specified era.
     * @param era the era the card belongs to
     * @param name the name identifying the {@code Character}'s subtype
     */
    public Character(Era era, String name) {
        super(era);
        this.TYPE = "Character";
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public String toString() {
        return "name: " + name + "\ntype: " + TYPE;
    }
}
