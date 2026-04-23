package it.polimi.ingsw.model.board;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;

public class OfferDTO implements Serializable {
    private char order;
    private int foodBonus;
    private int drawTop;
    private int drawBottom;

    @JsonCreator
    private OfferDTO() {}

    public OfferDTO(char order, int foodBonus, int drawTop, int drawBottom) throws IllegalArgumentException {
        this.order = order;
        this.foodBonus = foodBonus;
        this.drawTop = drawTop;
        this.drawBottom = drawBottom;
    }

    public char getOrder() {
        return order;
    }

    public int getDrawBottom() {
        return drawBottom;
    }

    public int getDrawTop() {
        return drawTop;
    }

    public int getFoodBonus() {
        return foodBonus;
    }
}
