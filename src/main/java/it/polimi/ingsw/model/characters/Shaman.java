package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.DTO.ShamanDTO;

/**
 * Represents the {@code Shaman} character.<br>
 * Each {@code Shaman} has a certain number of star icons.
 * During the {@code ShamanicRitual} event, the player with the most stars wins prestige points and the one with the least loses pps.
 */
public class Shaman extends Character {
    private final int stars;

    /**
     * Generates a {@code Shaman} object, filled with the specified parameters.
     * @param stars the number of star icons stated on the {@code Card}
     * @param era the {@code Era} the card belongs to
     */
    public Shaman(int stars, Era era, int id) {
        super(era, id);
        this.stars = stars;
        this.name = "Shaman";
    }

    public int getStars() {
        return stars;
    }

    @Override
    public void addToPlayer(Player player) {
        player.addShaman(this);
    }

    public ShamanDTO toDTO(){
        return new ShamanDTO(getEra().name(), getStars(), getId());
    }
}
