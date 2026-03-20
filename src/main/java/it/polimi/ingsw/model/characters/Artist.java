package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Character;

/**
 * Represents the Artist character
 */
public class Artist extends Character {

    public Artist(Era era) {
        this.name = "Artist";
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
     * Add an Artist to the tribe of the specified player
     * @param player
     */
    @Override
    public void addToPlayer(Player player) {
        player.addArtist(this);
    }
}
