package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.effects.Effect;
import it.polimi.ingsw.model.events.CavePaintings;
import it.polimi.ingsw.model.events.Hunt;
import it.polimi.ingsw.model.events.ShamanicRitual;
import it.polimi.ingsw.model.events.Sustenance;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {

    @Test
    void getTopRowTribe() {
        Board board = new Board(new Random(42));
        board.initialize(5);
        Card card1 = new Hunter(true, Era.II);
        Card card2 = new Builder(1, 2, Era.I);
        Card card3 = new ShamanicRitual(1, 3, Era.II);
        ArrayList<Card> row = new ArrayList<>(Arrays.asList(card1, card2, card3));
        board.setTopRowTribe(row);

        assertTrue(row.containsAll(board.getTopRowTribe()) && row.size() == board.getTopRowTribe().size());
    }

    @Test
    void drawFromTopRowTribe() throws IllegalActionException {
        Board board = new Board(new Random(42));
        board.initialize(5);
        Player player = new Player("Eli");
        Card card1 = new Hunter(true, Era.II);
        Card card2 = new Builder(1, 2, Era.I);
        Card card3 = new ShamanicRitual(1, 3, Era.II);
        Card card4 = new Builder(5, 3, Era.I);
        ArrayList<Card> row = new ArrayList<>(Arrays.asList(card1, card2, card3, card4));
        board.setTopRowTribe(row);

        assertThrows(IllegalArgumentException.class, ()->{board.drawFromTopRowTribe(-3);});
        assertThrows(IllegalArgumentException.class, ()->{board.drawFromTopRowTribe(4);});

        assertEquals(card4, board.drawFromTopRowTribe(3));
        assertEquals(4, board.getTopRowTribe().size());
        assertEquals(card1, board.drawFromTopRowTribe(0));
        assertEquals(card2, board.drawFromTopRowTribe(1));
        assertEquals(card3, board.drawFromTopRowTribe(2));

        //not allowed to draw events
        assertThrows(IllegalArgumentException.class, ()->{board.drawFromTopRowTribe(4);});

        //not allowed to draw from an empty row
        ArrayList<Card> emptyRow = new ArrayList<>(new ArrayList<>());
        board.setTopRowTribe(emptyRow);
        assertThrows(IllegalArgumentException.class, ()->{board.drawFromTopRowTribe(0);});
    }

    @Test
    void getBottomRowTribe() {
        Board board = new Board(new Random(42));
        board.initialize(5);
        Card card1 = new Hunter(true, Era.II);
        Card card2 = new Builder(1, 2, Era.I);
        Card card3 = new ShamanicRitual(1, 3, Era.II);
        ArrayList<Card> row = new ArrayList<>(Arrays.asList(card1, card2, card3));
        board.setBottomRowTribe(row);

        assertTrue(row.containsAll(board.getBottomRowTribe()) && row.size() == board.getBottomRowTribe().size());
    }

    @Test
    void drawFromBottomRowTribe() throws IllegalActionException {
        Board board = new Board(new Random(42));
        board.initialize(5);
        Card card1 = new Hunter(true, Era.II);
        Card card2 = new Builder(1, 2, Era.I);
        Card card3 = new ShamanicRitual(1, 3, Era.II);
        Card card4 = new Builder(5, 3, Era.I);
        ArrayList<Card> row = new ArrayList<>(Arrays.asList(card1, card2, card3, card4));
        board.setBottomRowTribe(row);

        assertThrows(IllegalArgumentException.class, ()->{board.drawFromBottomRowTribe(-3);});
        assertThrows(IllegalArgumentException.class, ()->{board.drawFromBottomRowTribe(4);});

        assertEquals(card4, board.drawFromBottomRowTribe(3));
        assertEquals(4, board.getBottomRowTribe().size());
        assertEquals(card1, board.drawFromBottomRowTribe(0));
        assertEquals(card2, board.drawFromBottomRowTribe(1));
        assertEquals(card3, board.drawFromBottomRowTribe(2));

        //not allowed to draw events
        assertThrows(IllegalArgumentException.class, ()->{board.drawFromBottomRowTribe(4);});

        //not allowed to draw from an empty row
        ArrayList<Card> emptyRow = new ArrayList<>(new ArrayList<>());
        board.setBottomRowTribe(emptyRow);
        assertThrows(IllegalArgumentException.class, ()->{board.drawFromBottomRowTribe(0);});
    }

    @Test
    void toDTO() {
        Board board = new Board(new Random(42));
        board.initialize(5);

        //Top Row Tribe
        Card card1 = new Hunter(true, Era.II);
        Card card2 = new Builder(1, 2, Era.I);
        Card card3 = new Artist(Era.I);
        Card card4 = new Gatherer(Era.I);
        Card card5 = new Inventor(Icon.ARROW, Era.II);
        Card card6 = new Shaman(1, Era.II);
        ArrayList<Card> tRow = new ArrayList<>(Arrays.asList(card1, card2, card3, card4, card5, card6));
        board.setTopRowTribe(tRow);

        //Bottom Row Tribe
        Card card7 = new ShamanicRitual(1, 3, Era.II);
        Card card8 = new CavePaintings(1, Era.II, 2, 3);
        Card card9 = new Hunt(1, Era.II);
        Card card10 = new Sustenance(3,  Era.II);
        ArrayList<Card> bRow = new ArrayList<>(Arrays.asList(card7, card8, card9, card10));
        board.setBottomRowTribe(bRow);

        //Top row building
        Building b = new Building(Era.I, 5, 5, 5, null, null, Effect.ECP);
        board.setTopRowBuilding(new ArrayList<>(Arrays.asList(b)));

        //Bottom row building
        Building b1 = new Building(Era.I, 5, 5, 5, null, null, Effect.EH);
        board.setBottomRowBuilding(new ArrayList<>(Arrays.asList(b1)));

        BoardDTO boardDTO = board.toDTO();
        //check the sizes
        assertTrue(board.getTopRowTribe().size() == boardDTO.getTopRowTribe().size());
        assertTrue(board.getBottomRowTribe().size() == boardDTO.getBottomRowTribe().size());
        assertTrue(board.getTopRowBuilding().size() == boardDTO.getTopRowBuilding().size());
        assertTrue(board.getBottomRowBuilding().size() == boardDTO.getBottomRowBuilding().size());

        assertTrue(board.getDeckTribe().size() == boardDTO.getDeckTribe().size());
        assertTrue(board.getDeckE1Building().size() == boardDTO.getDeckE1Building().size());
        assertTrue(board.getDeckE2Building().size() == boardDTO.getDeckE2Building().size());
        assertTrue(board.getDeckE3Building().size() == boardDTO.getDeckE3Building().size());

        //check the element
        for(int i = 0; i < board.getTopRowBuilding().size(); i++){
            assertTrue(boardDTO.getTopRowBuilding().get(i).getEffect().equals(board.getTopRowBuilding().get(i).getEffect().name()));
        }
        for(int i = 0; i < board.getBottomRowBuilding().size(); i++){
            assertTrue(boardDTO.getBottomRowBuilding().get(i).getEffect().equals(board.getBottomRowBuilding().get(i).getEffect().name()));
        }
        for(int i = 0; i < board.getTopRowTribe().size(); i++){
            assertTrue(boardDTO.getTopRowTribe().get(i).getName().equals(board.getTopRowTribe().get(i).getName()));
        }
        for(int i = 0; i < board.getBottomRowTribe().size(); i++){
            assertTrue(boardDTO.getBottomRowTribe().get(i).getName().equals(board.getBottomRowTribe().get(i).getName()));
        }

        Board newBoard = boardDTO.fromDTO();
        //check the element
        for(int i = 0; i < boardDTO.getTopRowBuilding().size(); i++){
            assertTrue(boardDTO.getTopRowBuilding().get(i).getEffect().equals(newBoard.getTopRowBuilding().get(i).getEffect().name()));
        }
        for(int i = 0; i < boardDTO.getBottomRowBuilding().size(); i++){
            assertTrue(boardDTO.getBottomRowBuilding().get(i).getEffect().equals(newBoard.getBottomRowBuilding().get(i).getEffect().name()));
        }
        for(int i = 0; i < boardDTO.getTopRowTribe().size(); i++){
            assertTrue(boardDTO.getTopRowTribe().get(i).getName().equals(newBoard.getTopRowTribe().get(i).getName()));
        }
        for(int i = 0; i < boardDTO.getBottomRowTribe().size(); i++){
            assertTrue(boardDTO.getBottomRowTribe().get(i).getName().equals(newBoard.getBottomRowTribe().get(i).getName()));
        }

        //check the sizes
        assertTrue(newBoard.getTopRowTribe().size() == boardDTO.getTopRowTribe().size());
        assertTrue(newBoard.getBottomRowTribe().size() == boardDTO.getBottomRowTribe().size());
        assertTrue(newBoard.getTopRowBuilding().size() == boardDTO.getTopRowBuilding().size());
        assertTrue(newBoard.getBottomRowBuilding().size() == boardDTO.getBottomRowBuilding().size());

        assertTrue(newBoard.getDeckTribe().size() == boardDTO.getDeckTribe().size());
        assertTrue(newBoard.getDeckE1Building().size() == boardDTO.getDeckE1Building().size());
        assertTrue(newBoard.getDeckE2Building().size() == boardDTO.getDeckE2Building().size());
        assertTrue(newBoard.getDeckE3Building().size() == boardDTO.getDeckE3Building().size());
    }

}