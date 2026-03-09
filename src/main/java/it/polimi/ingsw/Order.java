package it.polimi.ingsw;

public class Order extends Tile {
    private char order;
    private int numPlayers;
    private int foodBonus;
    private int pickTop;
    private int pickBottom;

    public Order(char order, int numPlayers, int foodBonus, int pickTop, int pickBottom) {
        this.order = order;
        this.numPlayers = numPlayers;
        this.foodBonus = foodBonus;
        this.pickTop = pickTop;
        this.pickBottom = pickBottom;
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

    public int getPickTop() {
        return pickTop;
    }

    public int getPickBottom() {
        return pickBottom;
    }
}
