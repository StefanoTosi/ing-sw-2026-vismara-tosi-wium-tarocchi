package it.polimi.ingsw;

import java.util.List;

public class Offer extends Tile {
    private List<Integer> foodBonus;
    private List<Integer> ppBonus;
    private int numPlayers;

    public Offer(int numPlayers, List<Integer> ppBonus, List<Integer> foodBonus) {
        this.numPlayers = numPlayers;
        this.ppBonus = ppBonus;
        this.foodBonus = foodBonus;
    }

    public int getFoodBonus(int pos) {
        return foodBonus.get(pos);
    }

    public int getPpBonus(int pos) {
        return ppBonus.get(pos);
    }

    public int getNumPlayers() {
        return numPlayers;
    }
}
