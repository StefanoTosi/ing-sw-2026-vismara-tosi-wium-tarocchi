package it.polimi.ingsw.model.board;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;
import java.util.List;

/**
 * Holds data about an {@code Order} tile in DTO format.
 */
public class OrderDTO implements Serializable {
    private List<Integer> foodBonus;
    private List<Integer> ppBonus;
    private int numPlayers;

    @JsonCreator
    private OrderDTO() {}

    /**
     * Generates an {@code OrderDTO} object, filled with specified parameters.
     * @param foodBonus the food awarded (or deducted if negative) when a totem is placed on the tile
     * @param ppBonus the pp awarded (or deducted if negative) when a totem is placed on the tile
     * @param numPlayers the number of players the tile accommodates
     */
    public OrderDTO(List<Integer> foodBonus, List<Integer> ppBonus, int numPlayers) {
        this.foodBonus = List.copyOf(foodBonus);
        this.ppBonus = List.copyOf(ppBonus);
        this.numPlayers = numPlayers;
    }

    public List<Integer> getFoodBonus() {
        return foodBonus;
    }

    public List<Integer> getPpBonus() {
        return ppBonus;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Converts the current tile from DTO format to a standard object.
     * @return the corresponding {@code Order} object
     */
    public Order fromDTO() {
        return new Order(this.numPlayers, this.ppBonus, this.foodBonus);
    }
}
