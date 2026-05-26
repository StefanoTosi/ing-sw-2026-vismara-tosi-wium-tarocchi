package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Card;

import java.util.*;

/**
 * Models a covered deck of cards.<br>
 * Decks can be shuffled and stacked. They can only be accessed through drawing one card at a time from the top of the deck.
 */
public class Deck {
    private final List<Card> deck;
    private Random rng;

    /**
     * Generates a {@code Deck} object from a list of Cards.<br>
     * This constructor is only used in testing as it requires the rng seed.
     * @param deck the list of {@code Card} objects
     * @param rng random number generator seed
     * @throws IllegalArgumentException if the specified list is {@code null}
     */
    public Deck(List<Card> deck, Random rng) throws IllegalArgumentException {
        if (deck == null) {
            throw new IllegalArgumentException("'deck' is null");
        }
        this.deck = deck;
        this.rng = rng;
    }

    /**
     * Generates a {@code Deck} object from a list of Cards.
     * @param deck the list of {@code Card} objects
     * @throws IllegalArgumentException if the specified list is {@code null}
     */
    public Deck(List<Card> deck) throws IllegalArgumentException {
        if (deck == null) {
            throw new IllegalArgumentException("'deck' is null");
        }
        this.deck = deck;
    }

    public List<Card> getDeck() {
        return deck;
    }

    /**
     * Shuffles the current deck, randomly permuting its list of cards.
     */
    public void shuffle() {
        Collections.shuffle(deck, rng);
    }

    /**
     * Draws the first card on top of the {@code Deck}, removing it from the deck's list of cards.
     * @return the {@code Card} on top of the {@code Deck}
     * @throws NoSuchElementException if the deck is {@code null}
     */
    public Card draw() throws NoSuchElementException {
        return deck.removeFirst();
    }

    /**
     * Generates a new deck, stacking the specified deck on top of the current one.
     * @param topDeck the deck to be placed on top
     * @return the resulting deck, where the specified deck is placed on top of the current one
     * @throws IllegalArgumentException if the specified deck is {@code null}
     */
    public Deck stack(Deck topDeck) throws IllegalArgumentException {
        if (topDeck == null) {
            throw new IllegalArgumentException("'topDeck' is null");
        }

        List<Card> d = new ArrayList<Card>(deck);
        d.addAll(0, topDeck.getDeck());
        return new Deck(d, rng);
    }

    /**
     * Returns the number of cards in the {@code Deck}
     * @return the size of the deck's list of cards
     */
    public int size() {
        return deck.size();
    }
}
