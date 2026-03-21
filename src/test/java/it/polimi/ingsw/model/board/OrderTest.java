package it.polimi.ingsw.model.board;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void Order() {
        assertThrows(IllegalArgumentException.class, () ->
            new Order(
                    1,
                    new ArrayList<>(List.of(new Integer[] {1, 2})),
                    new ArrayList<>(List.of(new Integer[] {3, 4}))
            )
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Order(
                        3,
                        new ArrayList<>(List.of(new Integer[] {1, 2})),
                        new ArrayList<>(List.of(new Integer[] {3, 4}))
                )
        );
    }

    @Test
    void getFoodBonus() {
        Order order = new Order(
                2,
                new ArrayList<>(List.of(new Integer[] {1, 2})),
                new ArrayList<>(List.of(new Integer[] {3, 4}))
        );

        assertEquals(3, order.getFoodBonus(0));
        assertEquals(4, order.getFoodBonus(1));
        assertThrows(IndexOutOfBoundsException.class, () -> order.getFoodBonus(2));
        assertThrows(IndexOutOfBoundsException.class, () -> order.getFoodBonus(-1));
    }

    @Test
    void getPpBonus() {
        Order order = new Order(
                2,
                new ArrayList<>(List.of(new Integer[] {1, 2})),
                new ArrayList<>(List.of(new Integer[] {3, 4}))
        );

        assertEquals(1, order.getPpBonus(0));
        assertEquals(2, order.getPpBonus(1));
        assertThrows(IndexOutOfBoundsException.class, () -> order.getPpBonus(2));
        assertThrows(IndexOutOfBoundsException.class, () -> order.getPpBonus(-1));
    }

    @Test
    void getNumPlayers() {
        Order order = new Order(
                2,
                new ArrayList<>(List.of(new Integer[] {1, 2})),
                new ArrayList<>(List.of(new Integer[] {3, 4}))
        );

        assertEquals(2, order.getNumPlayers());
    }
}