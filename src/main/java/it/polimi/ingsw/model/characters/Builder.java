package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.DTO.BuilderDTO;

/**
 * Represents the Builder character.
 * Each Builder reduces the Food
 * cost of every Building card, and at the end of the game provides the prestige points stated
 */
public class Builder extends Character {
    private final int foodDiscount;
    private final int pp;

    public Builder(int foodDiscount, int pp, Era era) {
        super(era);
        this.foodDiscount = foodDiscount;
        this.pp = pp;
        this.name = "Builder";
    }

    public int getFoodDiscount() {
        return foodDiscount;
    }

    public int getPp() {
        return pp;
    }

    /**
     * Add a Builder to the tribe of the specified player
     * @param player
     */
    @Override
    public void addToPlayer(Player player) {
        player.addBuilder(this);
    }

    public BuilderDTO toDTO(){
        return new BuilderDTO(getFoodDiscount(), getPp(), getEra().name());
    }
}
