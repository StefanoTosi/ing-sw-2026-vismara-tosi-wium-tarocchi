package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Card;

import java.util.List;

/**
 * Game board, holds information about all elements present on the board
 */
public class Board {
    private List<Card> topRow;
    private List<Card> bottomRow;

    private Deck deckTribe;
    private Deck deckE2Building;
    private Deck deckE3Building;

    private List<Tile> tiles;

    /**
     * Generates a starting board given the number of players
     * @param numPlayers
     */
    public Board(int numPlayers) {}

    public List<Card> getTopRow() {
        return topRow;
    }

    /**
     * Picks the card in the top row at position pos
     */
    public Card drawFromTopRow(int pos) throws IllegalArgumentException {
        if (pos < 0 || pos >= topRow.size()) {
            throw new IllegalArgumentException("'pos' is not a valid index");
        }
        return topRow.remove(pos);
    }

    public List<Card> getBottomRow() {
        return bottomRow;
    }

    /**
     * Picks the card in the bottom row at position pos
     */
    public Card drawFromBottomRow(int pos) throws IllegalArgumentException {
        if (pos < 0 || pos >= topRow.size()) {
            throw new IllegalArgumentException("'pos' is not a valid index");
        }
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
    
    private List<Card> loadCards() {
        return null;
    }
}
