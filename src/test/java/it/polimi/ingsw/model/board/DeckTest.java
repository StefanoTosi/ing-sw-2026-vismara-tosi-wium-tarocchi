package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.characters.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void Deck() {
        Deck deck = new Deck(new ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> new Deck(null));
    }

    @Test
    void shuffle() {
        Card card1 = new Hunter(true, Era.II);
        Card card2 = new Builder(1, 2, Era.I);
        Card card3 = new Inventor(Icon.ARROW, Era.III);
        Card card4 = new Artist(Era.II);
        Deck deck = new Deck(new ArrayList<>(Arrays.asList(card1, card2, card3, card4)));
        ArrayList<Card> originalDeck = new ArrayList<>(new ArrayList<>(Arrays.asList(card1, card2, card3, card4)));

        deck.shuffle();
        assertEquals(4, deck.size());

        while (deck.size() > 0) {
            Card card = deck.draw();
            boolean found = false;
            for(int j = 0; j < deck.size(); j++) {
                if(originalDeck.get(j).equals(deck.draw())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
        assertEquals(0, deck.size());

        Deck emptyDeck = new Deck(new ArrayList<>());
        emptyDeck.shuffle();
        assertThrows(NoSuchElementException.class, emptyDeck::draw);
    }

    @Test
    void draw() {
        Card card1 = new Hunter(true, Era.II);
        Card card2 = new Builder(1, 2, Era.I);
        Card card3 = new Hunter(true, Era.II);
        Deck deck = new Deck(new ArrayList<>(Arrays.asList(card1, card2, card3)));
        assertEquals(card1, deck.draw());
        assertEquals(card2, deck.draw());
        assertEquals(card3, deck.draw());
        assertThrows(NoSuchElementException.class, deck::draw);
    }

    @Test
    void stack() {
        Card card1 = new Hunter(true, Era.II);
        Card card2 = new Builder(1, 2, Era.I);
        Deck deck1 = new Deck(new ArrayList<>(Arrays.asList(card1, card2)));

        Card card3 = new Inventor(Icon.ARROW, Era.III);
        Card card4 = new Artist(Era.II);
        Deck deck2 = new Deck(new ArrayList<>(Arrays.asList(card3, card4)));

        deck2 = deck2.stack(deck1);
        assertEquals(card1, deck2.draw());
        assertEquals(card2, deck2.draw());
        assertEquals(card3, deck2.draw());
        assertEquals(card4, deck2.draw());

        assertThrows(NoSuchElementException.class, ()->{
            Deck deck3 =  new Deck(new ArrayList<>());
            deck3 = deck3.stack(deck1);
            assertEquals(card1, deck3.draw());
            assertEquals(card2, deck3.draw());
            deck3.draw();
        });

    }
}