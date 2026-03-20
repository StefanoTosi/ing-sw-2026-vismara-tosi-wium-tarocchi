package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;

/**
 * Represents the Builder character.
 * Each Builder reduces the Food
 * cost of every Building card, and at the end of the game provides the prestige points stated
 */
public class Builder extends Character {
    private int foodDiscount;
    private int pp;

    public Builder(int foodDiscount, int pp, Era era) {
        this.foodDiscount = foodDiscount;
        this.pp = pp;
        this.name = "Builder";
        this.era = era;
    }

    public int getFoodDiscount() {
        return foodDiscount;
    }

    public int getPp() {
        return pp;
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
     * Add a Builder to the tribe of the specified player
     * @param player
     */
    @Override
    public void addToPlayer(Player player) {
        player.addBuilder(this);
    }
}
