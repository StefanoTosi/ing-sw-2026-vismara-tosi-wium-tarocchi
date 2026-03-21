package it.polimi.ingsw.model;

/**
 * Define the common method of the Card,
 * either they are Characters, Buildings or Event
 */
public abstract class Card {
    protected Era era;
    protected String TYPE;

    public Card(Era era) {
        this.era = era;
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
    public String getType(){return TYPE;}

    /**
     * Add the card to the tribe of the specified player
     * @param player
     */
    public void addToPlayer (Player player){}
}
