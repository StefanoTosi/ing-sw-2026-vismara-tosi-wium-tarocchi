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
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {

    @Test
    void getTopRowTribe() {
        Board board = new Board(new Random(42));
        board.initialize(5);
        Card card1 = new Hunter(true, Era.II, 0);
        Card card2 = new Builder(1, 2, Era.I, 0);
        Card card3 = new ShamanicRitual(1, 3, Era.II, 0);
        ArrayList<Card> row = new ArrayList<>(Arrays.asList(card1, card2, card3));
        board.setTopRowTribe(row);

        assertTrue(row.containsAll(board.getTopRowTribe()) && row.size() == board.getTopRowTribe().size());
    }

    @Test
    void drawFromTopRowTribe() throws IllegalActionException {
        Board board = new Board(new Random(42));
        board.initialize(5);
        Player player = new Player("Eli");
        Card card1 = new Hunter(true, Era.II, 0);
        Card card2 = new Builder(1, 2, Era.I, 0);
        Card card3 = new ShamanicRitual(1, 3, Era.II, 0);
        Card card4 = new Builder(5, 3, Era.I, 0);
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
        Card card1 = new Hunter(true, Era.II, 0);
        Card card2 = new Builder(1, 2, Era.I, 0);
        Card card3 = new ShamanicRitual(1, 3, Era.II, 0);
        ArrayList<Card> row = new ArrayList<>(Arrays.asList(card1, card2, card3));
        board.setBottomRowTribe(row);

        assertTrue(row.containsAll(board.getBottomRowTribe()) && row.size() == board.getBottomRowTribe().size());
    }

    @Test
    void drawFromBottomRowTribe() throws IllegalActionException {
        Board board = new Board(new Random(42));
        board.initialize(5);
        Card card1 = new Hunter(true, Era.II, 0);
        Card card2 = new Builder(1, 2, Era.I, 0);
        Card card3 = new ShamanicRitual(1, 3, Era.II, 0);
        Card card4 = new Builder(5, 3, Era.I, 0);
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
    void drawFromTopRowBuilding() throws IllegalActionException {
        Board board = new Board(new Random(42));
        board.initialize(5);

        //Row does not contain buildings
        assertThrows(IllegalArgumentException.class, () -> board.drawFromTopRowBuilding(0));

        Building b1 = new Building(Era.I, 1, 2, 3, null, null, null, 0);
        board.setTopRowBuilding(List.of(b1));

        assertEquals(b1, board.drawFromTopRowBuilding(0));
        assertEquals(1, board.getTopRowBuilding().size());
    }

    @Test
    void drawFromBottomRowBuilding() throws IllegalActionException {
        Board board = new Board(new Random(42));
        board.initialize(5);

        //Row does not contain buildings
        assertThrows(IllegalArgumentException.class, () -> board.drawFromBottomRowBuilding(0));

        Building b1 = new Building(Era.I, 1, 2, 3, null, null, null, 0);
        board.setBottomRowBuilding(List.of(b1));

        assertEquals(b1, board.drawFromBottomRowBuilding(0));
        assertEquals(1, board.getBottomRowBuilding().size());
    }

    @Test
    void drawableCardsFromTop() {
        Board board = new Board(new Random(42));
        board.initialize(5);
        Player player = new Player("Eli");
        Card card1 = new Hunter(true, Era.II, 0);
        Card card2 = new Builder(1, 2, Era.I, 0);
        Card card3 = new ShamanicRitual(1, 3, Era.II, 0);
        Card card4 = new Builder(1, 3, Era.I, 0);
        ArrayList<Card> row = new ArrayList<>(Arrays.asList(card1, card2, card3, card4));
        board.setTopRowTribe(row);

        assertEquals(3, board.drawableCardsFromTop(player));

        //Row includes events and unaffordable buildings
        Card card5 = new Sustenance(1, Era.II, 0);
        Building card6 = new Building(Era.I, 2, 2, 0, null, null, null, 0);
        Building card7 = new Building(Era.I, 4, 2, 0, null, null, null, 0);
        row.add(card5);
        board.setBottomRowTribe(row);
        board.setTopRowBuilding(Arrays.asList(card6, card7));
        player.setFood(2);

        assertEquals(4, board.drawableCardsFromTop(player));
    }

    @Test
    void drawableCardsFromBottom() {
        Board board = new Board(new Random(42));
        board.initialize(5);
        Player player = new Player("Eli");
        Card card1 = new Hunter(true, Era.II, 0);
        Card card2 = new Builder(1, 2, Era.I, 0);
        Card card3 = new ShamanicRitual(1, 3, Era.II, 0);
        Card card4 = new Builder(5, 3, Era.I, 0);
        ArrayList<Card> row = new ArrayList<>(Arrays.asList(card1, card2, card3, card4));
        board.setBottomRowTribe(row);

        assertEquals(3, board.drawableCardsFromBottom(player));

        //Row includes events and unaffordable buildings
        Card card5 = new Sustenance(1, Era.II, 0);
        Building card6 = new Building(Era.I, 2, 2, 0, null, null, null, 0);
        Building card7 = new Building(Era.I, 4, 2, 0, null, null, null, 0);
        row.add(card5);
        board.setBottomRowTribe(row);
        board.setBottomRowBuilding(Arrays.asList(card6, card7));
        player.setFood(2);

        assertEquals(4, board.drawableCardsFromBottom(player));
    }

    @Test
    void toDTO() {
        Board board = new Board(new Random(42));
        board.initialize(5);

        //Top Row Tribe
        Card card1 = new Hunter(true, Era.II, 0);
        Card card2 = new Builder(1, 2, Era.I, 0);
        Card card3 = new Artist(Era.I, 0);
        Card card4 = new Gatherer(Era.I, 0);
        Card card5 = new Inventor(Icon.ARROW, Era.II, 0);
        Card card6 = new Shaman(1, Era.II, 0);
        ArrayList<Card> tRow = new ArrayList<>(Arrays.asList(card1, card2, card3, card4, card5, card6));
        board.setTopRowTribe(tRow);

        //Bottom Row Tribe
        Card card7 = new ShamanicRitual(1, 3, Era.II, 0);
        Card card8 = new CavePaintings(1, Era.II, 2, 3, 0);
        Card card9 = new Hunt(1, Era.II, 0);
        Card card10 = new Sustenance(3,  Era.II, 0);
        ArrayList<Card> bRow = new ArrayList<>(Arrays.asList(card7, card8, card9, card10));
        board.setBottomRowTribe(bRow);

        //Top row building
        Building b = new Building(Era.I, 5, 5, 5, null, null, Effect.ECP, 0);
        board.setTopRowBuilding(new ArrayList<>(Arrays.asList(b)));

        //Bottom row building
        Building b1 = new Building(Era.I, 5, 5, 5, null, null, Effect.EH, 0);
        board.setBottomRowBuilding(new ArrayList<>(Arrays.asList(b1)));

        BoardDTO boardDTO = board.toDTO();
        //check the sizes
        assertEquals(board.getTopRowTribe().size(), boardDTO.getTopRowTribe().size());
        assertEquals(board.getBottomRowTribe().size(), boardDTO.getBottomRowTribe().size());
        assertEquals(board.getTopRowBuilding().size(), boardDTO.getTopRowBuilding().size());
        assertEquals(board.getBottomRowBuilding().size(), boardDTO.getBottomRowBuilding().size());

        assertEquals(board.getDeckTribe().size(), boardDTO.getDeckTribe().size());
        assertEquals(board.getDeckE1Building().size(), boardDTO.getDeckE1Building().size());
        assertEquals(board.getDeckE2Building().size(), boardDTO.getDeckE2Building().size());
        assertEquals(board.getDeckE3Building().size(), boardDTO.getDeckE3Building().size());

        //check the element
        for(int i = 0; i < board.getTopRowBuilding().size(); i++){
            assertEquals(boardDTO.getTopRowBuilding().get(i).getEffect(), board.getTopRowBuilding().get(i).getEffect().name());
        }
        for(int i = 0; i < board.getBottomRowBuilding().size(); i++){
            assertEquals(boardDTO.getBottomRowBuilding().get(i).getEffect(), board.getBottomRowBuilding().get(i).getEffect().name());
        }
        for(int i = 0; i < board.getTopRowTribe().size(); i++){
            assertEquals(boardDTO.getTopRowTribe().get(i).getName(), board.getTopRowTribe().get(i).getName());
        }
        for(int i = 0; i < board.getBottomRowTribe().size(); i++){
            assertEquals(boardDTO.getBottomRowTribe().get(i).getName(), board.getBottomRowTribe().get(i).getName());
        }

        Board newBoard = boardDTO.fromDTO();
        //check the element
        for(int i = 0; i < boardDTO.getTopRowBuilding().size(); i++){
            assertEquals(boardDTO.getTopRowBuilding().get(i).getEffect(), newBoard.getTopRowBuilding().get(i).getEffect().name());
        }
        for(int i = 0; i < boardDTO.getBottomRowBuilding().size(); i++){
            assertEquals(boardDTO.getBottomRowBuilding().get(i).getEffect(), newBoard.getBottomRowBuilding().get(i).getEffect().name());
        }
        for(int i = 0; i < boardDTO.getTopRowTribe().size(); i++){
            assertEquals(boardDTO.getTopRowTribe().get(i).getName(), newBoard.getTopRowTribe().get(i).getName());
        }
        for(int i = 0; i < boardDTO.getBottomRowTribe().size(); i++){
            assertEquals(boardDTO.getBottomRowTribe().get(i).getName(), newBoard.getBottomRowTribe().get(i).getName());
        }

        //check the sizes
        assertEquals(newBoard.getTopRowTribe().size(), boardDTO.getTopRowTribe().size());
        assertEquals(newBoard.getBottomRowTribe().size(), boardDTO.getBottomRowTribe().size());
        assertEquals(newBoard.getTopRowBuilding().size(), boardDTO.getTopRowBuilding().size());
        assertEquals(newBoard.getBottomRowBuilding().size(), boardDTO.getBottomRowBuilding().size());

        assertEquals(newBoard.getDeckTribe().size(), boardDTO.getDeckTribe().size());
        assertEquals(newBoard.getDeckE1Building().size(), boardDTO.getDeckE1Building().size());
        assertEquals(newBoard.getDeckE2Building().size(), boardDTO.getDeckE2Building().size());
        assertEquals(newBoard.getDeckE3Building().size(), boardDTO.getDeckE3Building().size());
    }
}