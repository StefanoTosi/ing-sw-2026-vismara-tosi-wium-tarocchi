package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;

/**
 * Represents the Artist character
 */
public class Artist extends Character {

    public Artist(Era era) {
        super(era);
        this.name = "Artist";
    }

    /**
     * Add an Artist to the tribe of the specified player
     * @param player
     */
    @Override
    public void addToPlayer(Player player) {
        player.addArtist(this);
    }
}
