package it.polimi.ingsw;

/**
 * Represents the Gatherer character
 */
public class Gatherer extends Character {
    public Gatherer(Era era) {
        this.name = "Gatherer";
        this.era = era;
    }

    @Override
    public Era getEra() {
        return era;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Add a Gatherer to the tribe of the specified player
     * @param player
     */
    @Override
    public void addToPlayer(Player player) {
        player.addGatherer(this);
    }
}
