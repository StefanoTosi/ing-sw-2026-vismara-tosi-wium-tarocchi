package it.polimi.ingsw.model.board;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;

/**
 * Holds data about an {@code Offer} tile in DTO format.
 */
public class OfferDTO implements Serializable {
    private char order;
    private int foodBonus;
    private int drawTop;
    private int drawBottom;

    @JsonCreator
    private OfferDTO() {}

    /**
     * Generates an {@code OfferDTO} object, filled with specified parameters.
     * @param order the letter identifying the tile
     * @param foodBonus the amount of food awarded to the player
     * @param drawTop the amount of cards to be drawn from the top row
     * @param drawBottom the amount of cards to be drawn from the bottom row
     * @throws IllegalArgumentException if {@code order} is not a valid letter, or if {@code drawTop} or {@code drawBottom} are negative
     */
    public OfferDTO(char order, int foodBonus, int drawTop, int drawBottom) throws IllegalArgumentException {
        if (order < 'A' || order > 'G') {
            throw new IllegalArgumentException("'order' is not a valid letter");
        }

        if (drawTop < 0 || drawBottom < 0) {
            throw new IllegalArgumentException("'drawTop' and 'drawBottom' must be not negative");
        }

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

    /**
     * Converts the current tile from DTO format to a standard object.
     * @return the corresponding {@code Offer} object
     */
    public Offer fromDTO() {
        return new Offer(this.order, this.foodBonus, this.drawTop, this.drawBottom);
    }
}
