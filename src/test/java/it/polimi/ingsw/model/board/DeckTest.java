package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.characters.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void Deck() {
        Artist a = new Artist(Era.I, 0);
        Deck deck = new Deck(List.of(a), null);
        assertEquals(List.of(a), deck.getDeck());

        assertThrows(IllegalArgumentException.class, () -> new Deck(null, null));
        assertThrows(IllegalArgumentException.class, () -> new Deck(null));
    }

    @Test
    void shuffle() {
        Card card1 = new Hunter(true, Era.II, 0);
        Card card2 = new Builder(1, 2, Era.I, 0);
        Card card3 = new Inventor(Icon.ARROW, Era.III, 0);
        Card card4 = new Artist(Era.II, 0);
        Random rng = new Random(42);
        Deck deck = new Deck(new ArrayList<>(Arrays.asList(card1, card2, card3, card4)), rng);
        ArrayList<Card> originalDeck = new ArrayList<>(new ArrayList<>(Arrays.asList(card1, card2, card3, card4)));

        deck.shuffle();
        assertEquals(4, deck.size());

        while (deck.size() > 0) {
            Card card = deck.draw();
            assertTrue(originalDeck.contains(card));
        }
        assertEquals(0, deck.size());

        Deck emptyDeck = new Deck(new ArrayList<>(), null);
        emptyDeck.shuffle();
        assertThrows(NoSuchElementException.class, emptyDeck::draw);
    }

    @Test
    void draw() {
        Card card1 = new Hunter(true, Era.II, 0);
        Card card2 = new Builder(1, 2, Era.I, 0);
        Card card3 = new Hunter(true, Era.II, 0);
        Deck deck = new Deck(new ArrayList<>(Arrays.asList(card1, card2, card3)), null);
        assertEquals(card1, deck.draw());
        assertEquals(card2, deck.draw());
        assertEquals(card3, deck.draw());
        assertThrows(NoSuchElementException.class, deck::draw);
    }

    @Test
    void stack() {
        Card card1 = new Hunter(true, Era.II, 0);
        Card card2 = new Builder(1, 2, Era.I, 0);
        Deck deck1 = new Deck(new ArrayList<>(Arrays.asList(card1, card2)), null);

        Card card3 = new Inventor(Icon.ARROW, Era.III, 0);
        Card card4 = new Artist(Era.II, 0);
        Deck deck2 = new Deck(new ArrayList<>(Arrays.asList(card3, card4)), null);

        deck2 = deck2.stack(deck1);
        assertEquals(card1, deck2.draw());
        assertEquals(card2, deck2.draw());
        assertEquals(card3, deck2.draw());
        assertEquals(card4, deck2.draw());

        assertThrows(NoSuchElementException.class, ()->{
            Deck deck3 =  new Deck(new ArrayList<>(), null);
            deck3 = deck3.stack(deck1);
            assertEquals(card1, deck3.draw());
            assertEquals(card2, deck3.draw());
            deck3.draw();
        });

        //Top deck is null
        assertThrows(IllegalArgumentException.class, ()->{deck1.stack(null);});
    }
}