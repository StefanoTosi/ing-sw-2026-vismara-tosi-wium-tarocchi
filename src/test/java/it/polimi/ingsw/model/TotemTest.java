package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TotemTest {
    @Test
    void values() {
        Totem[] values = Totem.values();

        assertEquals(5, values.length);
        assertArrayEquals(
                new Totem[]{Totem.ORANGE, Totem.YELLOW, Totem.BLUE, Totem.PURPLE, Totem.WHITE},
                values
        );
    }

    @Test
    void getId() {
        assertEquals(0, Totem.ORANGE.getId());
        assertEquals(1, Totem.YELLOW.getId());
        assertEquals(2, Totem.BLUE.getId());
        assertEquals(3, Totem.PURPLE.getId());
        assertEquals(4, Totem.WHITE.getId());
    }

    @Test
    void getAscii() {
        assertEquals("\u001B[41m", Totem.ORANGE.getAscii());
        assertEquals("\u001B[43m", Totem.YELLOW.getAscii());
        assertEquals("\u001B[44m", Totem.BLUE.getAscii());
        assertEquals("\u001B[45m", Totem.PURPLE.getAscii());
        assertEquals("\u001B[47m", Totem.WHITE.getAscii());
    }

    @Test
    void getColor() {
        assertEquals("\u001B[38;5;208mOrange", Totem.ORANGE.getColor());
        assertEquals("\u001B[33mYellow", Totem.YELLOW.getColor());
        assertEquals("\u001B[34mBlue", Totem.BLUE.getColor());
        assertEquals("\u001B[35mPurple", Totem.PURPLE.getColor());
        assertEquals("\u001B[37mWhite", Totem.WHITE.getColor());
    }

    @Test
    void enumMethodsAreNotNullOrEmpty() {
        for (Totem t : Totem.values()) {
            assertNotNull(t.getAscii());
            assertNotNull(t.getColor());
        }
    }

    @Test
    void valueOf() {
        assertEquals(Totem.ORANGE, Totem.valueOf("ORANGE"));
        assertEquals(Totem.YELLOW, Totem.valueOf("YELLOW"));
        assertEquals(Totem.BLUE, Totem.valueOf("BLUE"));
        assertEquals(Totem.PURPLE, Totem.valueOf("PURPLE"));
        assertEquals(Totem.WHITE, Totem.valueOf("WHITE"));
    }
}
