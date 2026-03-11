package it.polimi.ingsw;

import java.util.*;

public class Deck {
    private List<Card> deck;

    private List<Card> getDeck() {
        return deck;
    }

    public Deck(List<Card> deck) {
        this.deck = deck;
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public Card pick() {
        return deck.removeFirst();
    }

    public Deck stack(Deck topDeck) {
        List<Card> d = new ArrayList<Card>(deck);
        d.addAll(0, topDeck.getDeck());
        return new Deck(d);
    }
}
