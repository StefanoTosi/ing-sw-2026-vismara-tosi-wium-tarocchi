package it.polimi.ingsw.model.board;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;
import java.util.List;

public class OrderDTO implements Serializable {
    private List<Integer> foodBonus;
    private List<Integer> ppBonus;
    private int numPlayers;

    @JsonCreator
    public OrderDTO() {}

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

}
