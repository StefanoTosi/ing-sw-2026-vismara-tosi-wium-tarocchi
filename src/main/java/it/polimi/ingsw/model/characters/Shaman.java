package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.DTO.ShamanDTO;

/**
 * Represents the Shaman character
 */
public class Shaman extends Character {
    private int stars;

    public Shaman(int stars, Era era) {
        super(era);
        this.stars = stars;
        this.name = "Shaman";
    }

    public int getStars() {
        return stars;
    }

    /**
     * Add a Shaman to the tribe of the specified player
     * @param player
     */
    @Override
    public void addToPlayer(Player player) {
        player.addShaman(this);
    }

    public ShamanDTO toDTO(){
        return new ShamanDTO(getEra().name(), getStars());
    }
}
