package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.DTO.InventorDTO;

/**
 * Represents an {@code Inventor} character.<br>
 * Each {@code Inventor} has an {@code Icon}. At the end of the game, each player is awarded an amount of prestige points equal to
 * the number of {@code Inventors} multiplied by the number of different invention icons in the tribe.
 */
public class Inventor extends Character {
    private final Icon inventionIcon;

    /**
     * Generates a {@code Hunter} object, filled with the specified parameters.
     * @param inventionIcon
     * @param era the {@code Era} the card belongs to
     */
    public Inventor(Icon inventionIcon, Era era) {
        super(era);
        this.inventionIcon = inventionIcon;
        this.name = "Inventor";
    }

    public Icon getInventionIcon() {
        return inventionIcon;
    }

    @Override
    public void addToPlayer(Player player) {
        player.addInventor(this);
    }

    public InventorDTO toDTO(){
        return new InventorDTO(getEra().name(), getInventionIcon(), getId());
    }
}
