package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.events.ShamanicRitual;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void getTopRowTribe() {
        Board board = new Board(5);
        Card card1 = new Hunter(true, Era.II);
        Card card2 = new Builder(1, 2, Era.I);
        Card card3 = new ShamanicRitual(1, 3, Era.II);
        ArrayList<Card> row = new ArrayList<>(new ArrayList<>(Arrays.asList(card1, card2, card3)));
        assertEquals(row, board.getTopRowTribe());
    }

    @Test
    void drawFromTopRowTribe() {
        Board board = new Board(5);
        Card card1 = new Hunter(true, Era.II);
        Card card2 = new Builder(1, 2, Era.I);
        Card card3 = new ShamanicRitual(1, 3, Era.II);
        Card card4 = new Builder(5, 3, Era.I);
        ArrayList<Card> row = new ArrayList<>(new ArrayList<>(Arrays.asList(card1, card2, card3)));
        board.setTopRowTribe(row);

        assertThrows(IllegalArgumentException.class, ()->{board.drawFromTopRowTribe(-3);});
        assertThrows(IllegalArgumentException.class, ()->{board.drawFromTopRowTribe(4);});

        assertEquals(card4, board.drawFromTopRowTribe(3));
        assertEquals(3, board.getTopRowTribe().size());
        assertEquals(card1, board.drawFromTopRowTribe(0));
        assertEquals(card2, board.drawFromTopRowTribe(0));

        //not allowed to draw events
        assertThrows(IllegalArgumentException.class, ()->{board.drawFromTopRowTribe(0);});

        //not allowed to draw from an empty row
        ArrayList<Card> emptyRow = new ArrayList<>(new ArrayList<>());
        board.setTopRowTribe(emptyRow);
        assertThrows(IllegalArgumentException.class, ()->{board.drawFromTopRowTribe(0);});
    }

    @Test
    void getBottomRowTribe() {
        Board board = new Board(5);
        Card card1 = new Hunter(true, Era.II);
        Card card2 = new Builder(1, 2, Era.I);
        Card card3 = new ShamanicRitual(1, 3, Era.II);
        ArrayList<Card> row = new ArrayList<>(new ArrayList<>(Arrays.asList(card1, card2, card3)));
        assertEquals(row, board.getBottomRowTribe());
    }

    @Test
    void drawFromBottomRowTribe() {
        Board board = new Board(5);
        Card card1 = new Hunter(true, Era.II);
        Card card2 = new Builder(1, 2, Era.I);
        Card card3 = new ShamanicRitual(1, 3, Era.II);
        Card card4 = new Builder(5, 3, Era.I);
        ArrayList<Card> row = new ArrayList<>(new ArrayList<>(Arrays.asList(card1, card2, card3)));
        board.setBottomRowTribe(row);

        assertThrows(IllegalArgumentException.class, ()->{board.drawFromBottomRowTribe(-3);});
        assertThrows(IllegalArgumentException.class, ()->{board.drawFromBottomRowTribe(4);});

        assertEquals(card4, board.drawFromBottomRowTribe(3));
        assertEquals(3, board.getBottomRowTribe().size());
        assertEquals(card1, board.drawFromBottomRowTribe(0));
        assertEquals(card2, board.drawFromBottomRowTribe(0));

        //not allowed to draw events
        assertThrows(IllegalArgumentException.class, ()->{board.drawFromBottomRowTribe(0);});

        //not allowed to draw from an empty row
        ArrayList<Card> emptyRow = new ArrayList<>(new ArrayList<>());
        board.setBottomRowTribe(emptyRow);
        assertThrows(IllegalArgumentException.class, ()->{board.drawFromBottomRowTribe(0);});
    }

    @Test
    void getDeckTribe() {
    }

    @Test
    void getDeckE2Building() {
    }

    @Test
    void getDeckE3Building() {
    }

    @Test
    void getTiles() {
    }
}