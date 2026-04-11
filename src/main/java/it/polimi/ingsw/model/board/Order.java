package it.polimi.ingsw.model.board;

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
    public Order(int numPlayers, List<Integer> ppBonus, List<Integer> foodBonus) throws IllegalArgumentException {
        if (numPlayers != ppBonus.size() || numPlayers != foodBonus.size()) {
            throw new IllegalArgumentException("'numPlayers' does not correspond with the size of ppBonus or foodBonus: " + numPlayers + ", " + ppBonus.size() + ", " + foodBonus.size());
        }

        this.numPlayers = numPlayers;
        this.ppBonus = ppBonus;
        this.foodBonus = foodBonus;
    }

    /**
     * Returns the food bonus for the player at position pos
     */
    public int getFoodBonus(int pos) throws IndexOutOfBoundsException {
        return foodBonus.get(pos);
    }

    /**
     * Returns the prestige bonus for the player at position pos
     */
    public int getPpBonus(int pos) throws IndexOutOfBoundsException {
        return ppBonus.get(pos);
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public OrderDTO toDTO(){
        return new OrderDTO(this.foodBonus, this.ppBonus, this.numPlayers);
    }
}
