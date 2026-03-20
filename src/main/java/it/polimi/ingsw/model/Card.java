package it.polimi.ingsw.model;

/**
 * Define the common method of the Card,
 * either they are Characters, Buildings or Event
 */
public interface Card {
    /**
     * Return the era the card belongs to
     * @return  Era
     */
    public Era getEra();

    /**
     * Return the name of the card
     * @return string
     */
    public String getName();

    /**
     * Return the type of the card: Characters, Buildings or Event
     * @return type
     */
    public String getType();

    /**
     * Add the card to the tribe of the specified player
     * @param player
     */
    public void addToPlayer (Player player);
}
