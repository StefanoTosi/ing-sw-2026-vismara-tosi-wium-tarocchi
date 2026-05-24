package it.polimi.ingsw.model.board;

import java.util.List;

/**
 * Represents the order tile, where the totems are placed at the start of a new turn, before being moved on the offer tiles.<br>
 * Each tile awards or deducts a certain amount of food and prestige points.
 */
public class Order implements Tile {
    private final List<Integer> foodBonus;
    private final List<Integer> ppBonus;
    private final int numPlayers;

    /**
     * Generates an order tile with the specified parameters.
     * @param numPlayers the number of players the tile accommodates
     * @param ppBonus the pp awarded (or deducted if negative) when a totem is placed on the tile
     * @param foodBonus the food awarded (or deducted if negative) when a totem is placed on the tile
     * @throws IllegalArgumentException if the number of players does not correspond to the size of specified {@code foodBonus} and {@code ppBonus} lists
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
     * Returns the food bonus or malus for the player at specified position
     * @param pos the position of the player's totem on the order tile
     * @return the amount of food to be awarded to (or deducted from, if negative) the player
     * @throws IndexOutOfBoundsException if the specified position is not valid
     */
    public int getFoodBonus(int pos) throws IndexOutOfBoundsException {
        return foodBonus.get(pos);
    }

    /**
     * Returns the prestige points bonus or malus for the player at specified position
     * @param pos the position of the player's totem on the order tile
     * @return the amount of prestige points to be awarded to (or deducted from, if negative) the player
     * @throws IndexOutOfBoundsException if the specified position is not valid
     */
    public int getPpBonus(int pos) throws IndexOutOfBoundsException {
        return ppBonus.get(pos);
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Converts the current tile into DTO format.
     * @return the corresponding {@code OrderDTO} object
     */
    public OrderDTO toDTO(){
        return new OrderDTO(this.foodBonus, this.ppBonus, this.numPlayers);
    }
}
