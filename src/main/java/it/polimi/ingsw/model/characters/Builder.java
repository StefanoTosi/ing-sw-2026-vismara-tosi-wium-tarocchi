package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.DTO.BuilderDTO;

/**
 * Represents the {@code Builder} character.<br>
 * Each Builder reduces the food cost of every {@code Building} card and at the end of the game provides the stated prestige points.
 */
public class Builder extends Character {
    private final int foodDiscount;
    private final int pp;

    /**
     * Generates a {@code Builder} object, filled with the specified parameters.
     * @param foodDiscount the amount of food the {@code Builder} allows to subtract from {@code Building} prices. This parameter should be positive.
     * @param pp the amount of prestige points awarded at the end of the game
     * @param era the {@code Era} the card belongs to
     */
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

    @Override
    public void addToPlayer(Player player) {
        player.addBuilder(this);
    }

    public BuilderDTO toDTO(){
        return new BuilderDTO(getFoodDiscount(), getPp(), getEra().name(), getId());
    }
}
