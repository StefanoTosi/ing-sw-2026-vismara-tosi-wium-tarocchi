package it.polimi.ingsw;

import java.util.List;

/**
 * Order tile
 */
public class Order implements Tile {
    private final List<Integer> foodBonus;
    private final List<Integer> ppBonus;
    private final int numPlayers;

    /**
     * Generates an order tile
     * @param numPlayers how many players does it accommodate
     * @param ppBonus what is the pp bonus for each spot on the tile
     * @param foodBonus what is the food bonus for each sport on the tile
     */
    public Order(int numPlayers, List<Integer> ppBonus, List<Integer> foodBonus) {
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
