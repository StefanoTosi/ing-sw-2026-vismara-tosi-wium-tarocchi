package it.polimi.ingsw.model.board;

/**
 * Offer tile
 */
public class Offer implements Tile {
    private final char order;
    private final int numPlayers;
    private final int foodBonus;
    private final int drawTop;
    private final int drawBottom;

    /**
     * Generates an offer tile
     * @param order ordering letter
     * @param numPlayers at least how many players can it be used with
     * @param foodBonus how much food does the player get
     * @param drawTop how many cards can be drawn from the top row
     * @param drawBottom how many cards can be drawn from the top row
     */
    public Offer(char order, int numPlayers, int foodBonus, int drawTop, int drawBottom) throws IllegalArgumentException {
        if (order < 'A' || order > 'G') {
            throw new IllegalArgumentException("'order' is not a valid letter");
        }

        if (numPlayers < 0 || drawTop < 0 || drawBottom < 0) {
            throw new IllegalArgumentException("Invalid negative argument");
        }

        this.order = order;
        this.numPlayers = numPlayers;
        this.foodBonus = foodBonus;
        this.drawTop = drawTop;
        this.drawBottom = drawBottom;
    }

    public char getOrder() {
        return order;
    }

    public int getNumPlayers() {
        return numPlayers;
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
}
