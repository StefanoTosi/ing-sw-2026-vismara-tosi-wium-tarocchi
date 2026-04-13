package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.DTO.GathererDTO;

/**
 * Represents the Gatherer character
 */
public class Gatherer extends Character {
    public Gatherer(Era era) {
        super(era);
        this.name = "Gatherer";
    }

    /**
     * Add a Gatherer to the tribe of the specified player
     * @param player
     */
    @Override
    public void addToPlayer(Player player) {
        player.addGatherer(this);
    }

    public GathererDTO toDTO(){
        return new GathererDTO(getEra().name());
    }
}
