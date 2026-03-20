package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Character;

/**
 * Represents the Shaman character
 */
public class Shaman extends Character {
    private int stars;

    public Shaman(int stars, Era era) {
        this.stars = stars;
        this.name = "Shaman";
    }

    public int getStars() {
        return stars;
    }

    /**
     * Characters doesn't have an era
     * @return null
     */
    @Override
    public Era getEra() {
        return era;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Add a Shaman to the tribe of the specified player
     * @param player
     */
    @Override
    public void addToPlayer(Player player) {
        player.addShaman(this);
    }
}
