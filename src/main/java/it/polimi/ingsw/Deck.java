package it.polimi.ingsw;

import java.util.*;

/**
 * Models a covered deck of cards
 */
public class Deck {
    private List<Card> deck;

    private List<Card> getDeck() {
        return deck;
    }

    public Deck(List<Card> deck) {
        this.deck = deck;
    }

    /**
     * Shuffles the deck
     */
    public void shuffle() {
        Collections.shuffle(deck);
    }

    /**
     * Draws the first card of the deck
     * @return
     */
    public Card draw() {
        return deck.removeFirst();
    }

    /**
     * Generates a new deck where this is at the bottom and topDeck is at the ´top´
     * @param topDeck
     * @return
     */
    public Deck stack(Deck topDeck) {
        List<Card> d = new ArrayList<Card>(deck);
        d.addAll(0, topDeck.getDeck());
        return new Deck(d);
    }
}
