package it.polimi.ingsw;

/**
 * Represents the Artist character
 */
public class Artist extends Character {

    public Artist() {
        this.name = "Artist";
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
