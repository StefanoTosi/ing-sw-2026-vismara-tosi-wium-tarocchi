package it.polimi.ingsw;

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

    @Override
    public Era getEra() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void applyEffect(Player p) {

    }
}
