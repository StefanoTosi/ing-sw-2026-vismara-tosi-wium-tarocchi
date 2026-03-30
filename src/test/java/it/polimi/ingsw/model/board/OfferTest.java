package it.polimi.ingsw.model.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfferTest {

    @Test
    void Order() {
        assertThrows(IllegalArgumentException.class, () -> new Offer('H', -1, 1, 2));
        assertThrows(IllegalArgumentException.class, () -> new Offer('A', 5, 1, -2));
        assertThrows(IllegalArgumentException.class, () -> new Offer('\0', 5, 1, 2));
    }

    @Test
    void getOrder() {
        Offer offer = new Offer('A', 5, 1, 2);
        assertEquals('A', offer.getOrder());
    }

    @Test
    void getFoodBonus() {
        Offer offer = new Offer('A', 5, 1, 2);
        assertEquals(5, offer.getFoodBonus());
    }

    @Test
    void getDrawTop() {
        Offer offer = new Offer('A', 5, 1, 2);
        assertEquals(1, offer.getDrawTop());
    }

    @Test
    void getDrawBottom() {
        Offer offer = new Offer('A', 5, 1, 2);
        assertEquals(2, offer.getDrawBottom());
    }
}