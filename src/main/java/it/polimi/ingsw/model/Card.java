package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.IllegalActionException;

/**
 * Defines the common methods of all Cards,
 * which are either {@code Characters}, {@code Buildings} or {@code Events}
 */
public abstract class Card {
    protected Era era;
    protected String TYPE;
    protected int id; // Number used to get the corresponding texture

    /**
     * Generates a {@code Card} object belonging to the specified era.
     * @param era the era the card belongs to
     */
    public Card(Era era) {
        this.era = era;
    }

    /**
     * Generates a {@code Card} object, whose subtype is known, belonging to the specified era and identified by an id number.
     * @param era the era the card belongs to
     * @param TYPE the name identifying the card's subtype
     * @param id number which identifies the card, used to get the corresponding texture
     */
    public Card(Era era, String TYPE, int id) {
        this.era = era;
        this.TYPE = TYPE;
        this.id = id;
    }

    public Era getEra(){
        return era;
    }

    public String getName(){
        return null;
    }

    public String getType(){
        return TYPE;
    }

    /**
     * Adds the card to the tribe of the specified player.
     * @param player the player who is drawing the card
     * @throws IllegalActionException if the card is an event or unaffordable building
     */
    public void addToPlayer (Player player) throws IllegalActionException {}

    /**
     * Converts the current card into the corresponding DTO class.
     * @return the corresponding card in DTO format
     */
    public CardDTO toDTO(){
        return new CardDTO(getEra().name(), getType(), getName(), getId());
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}