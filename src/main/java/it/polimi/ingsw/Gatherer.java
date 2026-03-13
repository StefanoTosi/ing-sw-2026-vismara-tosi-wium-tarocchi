package it.polimi.ingsw;

/**
 * Represents the Gatherer character
 */
public class Gatherer extends Character {
    public Gatherer() {
        this.name = "Gatherer";
    }

    /**
     * Characters doesn't have an era
     * @return null
     */
    @Override
    public Era getEra() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
}
