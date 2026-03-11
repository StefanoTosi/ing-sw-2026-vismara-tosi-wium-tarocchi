package it.polimi.ingsw;

import java.util.List;

public class Board {
    private List<Card> topRow;
    private List<Card> bottomRow;

    private Deck deckTribe;
    private Deck deckE2Building;
    private Deck deckE3Building;

    private List<Tile> tiles;

    public Board(int numPlayers) {}

    public List<Card> getTopRow() {
        return topRow;
    }

    public Card pickFromTopRow(int pos) {
        return topRow.remove(pos);
    }

    public List<Card> getBotomRow() {
        return topRow;
    }

    public Card pickFromBottomRow(int pos) {
        return bottomRow.remove(pos);
    }

    public Deck getDeckTribe() {
        return deckTribe;
    }

    public Deck getDeckE2Building() {
        return deckE2Building;
    }

    public Deck getDeckE3Building() {
        return deckE3Building;
    }

    public List<Tile> getTiles() {
        return tiles;
    }
}
