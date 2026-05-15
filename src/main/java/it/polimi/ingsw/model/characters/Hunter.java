package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.DTO.HunterDTO;

/**
 * Represents the Hunter character.
 * Each Hunter can have an icon that will eventually provide you extra food
 */
public class Hunter extends Character {
    private final boolean icon;

    public Hunter(boolean icon, Era era) {
        super(era);
        this.name = "Hunter";
        this.icon = icon;
    }

    public boolean getIcon() {
        return icon;
    }

    /**
     * Add a Hunter to the tribe of the specified player
     * @param player
     */
    @Override
    public void addToPlayer(Player player) {
        player.addHunter(this);
    }

    public HunterDTO toDTO(){
        return new HunterDTO(getIcon(), getEra().name(), getId());
    }
}
