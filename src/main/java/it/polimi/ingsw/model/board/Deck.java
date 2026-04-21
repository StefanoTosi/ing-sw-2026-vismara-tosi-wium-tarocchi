package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Card;

import java.util.*;

/**
 * Models a covered deck of cards
 */
public class Deck {
    private final List<Card> deck;
    private Random rng;

    private List<Card> getDeck() {
        return deck;
    }

    public Deck(List<Card> deck, Random rng) throws IllegalArgumentException {
        if (deck == null) {
            throw new IllegalArgumentException("'deck' is null");
        }
        this.deck = deck;
        this.rng = rng;
    }

    /**
     * Shuffles the deck
     */
    public void shuffle() {
        Collections.shuffle(deck, rng);
    }

    /**
     * Draws the first card of the deck
     */
    public Card draw() throws NoSuchElementException {
        return deck.removeFirst();
    }

    /**
     * Generates a new deck where this is at the bottom and topDeck is at the ´top´
     */
    public Deck stack(Deck topDeck) throws IllegalArgumentException {
        if (topDeck == null) {
            throw new IllegalArgumentException("'topDeck' is null");
        }

        List<Card> d = new ArrayList<Card>(deck);
        d.addAll(0, topDeck.getDeck());
        return new Deck(d, rng);
    }

    public int size() {
        return deck.size();
    }
}
