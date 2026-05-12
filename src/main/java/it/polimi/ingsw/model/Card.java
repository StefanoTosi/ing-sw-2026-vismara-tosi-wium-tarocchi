package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.IllegalActionException;

/**
 * Define the common method of the Card,
 * either they are Characters, Buildings or Events
 */

public abstract class Card {
    protected Era era;
    protected String TYPE;
    protected int id; // Number used to get the corresponding texture

    public Card(Era era) {
        this.era = era;
    }

    public Card(Era era, String TYPE, int id) {
        this.era = era;
        this.TYPE = TYPE;
        this.id = id;
    }

    /**
     * Return the era the card belongs to
     * @return  Era
     */
    public Era getEra(){
        return era;
    }

    /**
     * Return the name of the card
     * @return string
     */
    public String getName(){
        return null;
    }

    /**
     * Return the type of the card: Characters, Buildings or Event
     * @return type
     */
    public String getType(){
        return TYPE;
    }

    /**
     * Add the card to the tribe of the specified player
     * @param player
     */
    public void addToPlayer (Player player) throws IllegalActionException {}

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