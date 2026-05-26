package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.DTO.GathererDTO;

/**
 * Represents the {@code Gatherer} character.<br>
 * {@code Gatherers} provide a discount on the amount of food required during the {@code Sustenance} event.
 */
public class Gatherer extends Character {

    /**
     * Generates a {@code Gatherer} object, belonging to the specified era.
     * @param era the {@code Era} the card belongs to
     */
    public Gatherer(Era era) {
        super(era);
        this.name = "Gatherer";
    }

    @Override
    public void addToPlayer(Player player) {
        player.addGatherer(this);
    }

    public GathererDTO toDTO(){
        return new GathererDTO(getEra().name(), getId());
    }
}
