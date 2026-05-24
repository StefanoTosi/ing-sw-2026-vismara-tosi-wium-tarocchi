package it.polimi.ingsw.model.board;

/**
 * Represents an offer tile, where players' totems can be placed.<br>
 * Each offer tile allows players to draw a certain amount of cards from the top or bottom row, or can alternatively award food.
 */
public class Offer implements Tile {
    private final char order;
    private final int foodBonus;
    private final int drawTop;
    private final int drawBottom;

    /**
     * Generates an offer tile with specified parameters.
     * @param order ordering letter which identifies the tile
     * @param foodBonus the amount of food awarded to the player
     * @param drawTop amount of cards to be drawn from the top row
     * @param drawBottom amount of cards to be drawn from the bottom row
     * @throws IllegalArgumentException if {@code order} is not a valid letter or if {@code drawTop} or {@code drawBottom} are negative numbers
     */
    public Offer(char order, int foodBonus, int drawTop, int drawBottom) throws IllegalArgumentException {
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

    public int getFoodBonus() {
        return foodBonus;
    }

    public int getDrawTop() {
        return drawTop;
    }

    public int getDrawBottom() {
        return drawBottom;
    }

    /**
     * Converts the current tile into DTO format.
     * @return the corresponding {@code OfferDTO} object
     */
    public OfferDTO toDTO(){
        return new OfferDTO(getOrder(), getFoodBonus(), getDrawTop(), getDrawBottom());
    }
}
