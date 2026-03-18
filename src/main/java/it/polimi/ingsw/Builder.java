package it.polimi.ingsw;

/**
 * Represents the Builder character.
 * Each Builder reduces the Food
 * cost of every Building card, and at the end of the game provides the prestige points stated
 */
public class Builder extends Character {
    private int foodDiscount;
    private int pp;

    public Builder(int foodDiscount, int pp) {
        this.foodDiscount = foodDiscount;
        this.pp = pp;
        this.name = "Builder";
    }

    public Builder() {
        this.name = "Builder";
    }

    public int getFoodDiscount() {
        return foodDiscount;
    }

    public void setFoodDiscount(int foodDiscount) {
        this.foodDiscount = foodDiscount;
    }

    public int getPp() {
        return pp;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }
    /**
     * Characters doesn't have an era
     * @return null
     */
    @Override
    public Era getEra() {
        return null;
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
