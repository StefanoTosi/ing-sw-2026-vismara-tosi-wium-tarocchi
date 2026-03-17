package it.polimi.ingsw;

/**
 * Offer tile
 */
public class Offer implements Tile {
    private char order;
    private int numPlayers;
    private int foodBonus;
    private final int drawTop;
    private final int drawBottom;

    /**
     * Generates an offer tile
     * @param order ordering letter
     * @param numPlayers at least how many players can it be used with
     * @param foodBonus how much food does the player get
     * @param pickTop how many cards can be drawn from the top row
     * @param pickBottom how many cards can be drawn from the top row
     */
    public Offer(char order, int numPlayers, int foodBonus, int pickTop, int pickBottom) {
        this.order = order;
        this.numPlayers = numPlayers;
        this.foodBonus = foodBonus;
        this.drawTop = pickTop;
        this.drawBottom = pickBottom;
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
