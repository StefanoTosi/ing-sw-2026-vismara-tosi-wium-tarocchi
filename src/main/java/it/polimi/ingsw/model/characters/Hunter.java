package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.DTO.HunterDTO;

/**
 * Represents the {@code Hunter} character.<br>
 * Each {@code Hunter} can have a food icon, in which case, when the card is drawn, the player is awarded 1 food token for each {@code Hunter} in their tribe.
 * {@code Hunters} also award food and prestige points during the {@code Hunt} event.
 */
public class Hunter extends Character {
    private final boolean icon;

    /**
     * Generates a {@code Hunter} object, filled with the specified parameters.
     * @param icon {@code true} if the {@code Hunter} awards food when drawn, {@code false} otherwise
     * @param era the {@code Era} the card belongs to
     */
    public Hunter(boolean icon, Era era, int id) {
        super(era, id);
        this.name = "Hunter";
        this.icon = icon;
    }

    public boolean getIcon() {
        return icon;
    }

    /**
     * Adds the {@code Hunter} to the tribe of the specified player, awarding food tokens if necessary.
     * @param player the {@code Player} drawing the card
     */
    @Override
    public void addToPlayer(Player player) {
        player.addHunter(this);
    }

    public HunterDTO toDTO(){
        return new HunterDTO(getIcon(), getEra().name(), getId());
    }
}
