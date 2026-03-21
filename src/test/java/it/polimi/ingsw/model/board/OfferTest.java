package it.polimi.ingsw.model.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfferTest {

    @Test
    void Order() {
        assertThrows(IllegalArgumentException.class, () -> new Offer('\0', 5, 1, 2, 3));
        assertThrows(IllegalArgumentException.class, () -> new Offer('H', -1, 1, 2, 3));
        assertThrows(IllegalArgumentException.class, () -> new Offer('A', 5, 1, -2, 3));
        assertThrows(IllegalArgumentException.class, () -> new Offer('A', 5, 1, 2, -3));
    }

    @Test
    void getOrder() {
        Offer offer = new Offer('A', 5, 1, 2, 3);
        assertEquals('A', offer.getOrder());
    }

    @Test
    void getNumPlayers() {
        Offer offer = new Offer('A', 5, 1, 2, 3);
        assertEquals(5, offer.getNumPlayers());
    }

    @Test
    void getFoodBonus() {
        Offer offer = new Offer('A', 5, 1, 2, 3);
        assertEquals(1, offer.getFoodBonus());
    }

    @Test
    void getDrawTop() {
        Offer offer = new Offer('A', 5, 1, 2, 3);
        assertEquals(2, offer.getDrawTop());
    }

    @Test
    void getDrawBottom() {
        Offer offer = new Offer('A', 5, 1, 2, 3);
        assertEquals(3, offer.getDrawBottom());
    }
}