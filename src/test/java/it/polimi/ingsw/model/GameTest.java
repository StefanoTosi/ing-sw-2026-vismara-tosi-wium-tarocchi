package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.characters.Artist;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void getNumPlayers() throws IllegalArgumentException {
        Player player1 = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player1, player2, player3)));
        assertEquals(3, game.getNumPlayers());
    }

    @Test
    void getPlayers() throws IllegalArgumentException{
        Player player1 = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player1, player2, player3)));
        List<Player> test = Arrays.asList(player1, player2, player3);
        assert game.getPlayers().equals(test);
    }

    @Test
    void getPlayerPositions() throws IllegalArgumentException{
        Player player1 = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player1, player2, player3)));
        game.putPlayerPosition(player1, 'A');
        game.putPlayerPosition(player2, 'B');
        game.putPlayerPosition(player3, 'C');
    }

    @Test
    void putPlayerPosition() throws IllegalArgumentException {
        Player player1 = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player1, player2, player3)));
        game.putPlayerPosition(player1, 'A');
        game.putPlayerPosition(player2, 'B');
        game.putPlayerPosition(player3, 'C');
    }

    @Test
    void getBoard() throws IllegalArgumentException{
        Player player1 = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player1, player2, player3)));
        Board board = new Board(3);
        Card card = new Artist(Era.II);
        Card card2 = new Artist(Era.II);
        Card card3 = new Artist(Era.II);
        ArrayList<Card> topRowTribe = new ArrayList<Card>(Arrays.asList(card, card2, card3));
        board.setTopRowTribe(topRowTribe);
        game.getBoard().setTopRowTribe(topRowTribe);
        assertEquals(board.getTopRowTribe().size(), game.getBoard().getTopRowTribe().size());
    }

    @Test
    void getState() throws IllegalArgumentException {
        Player player1 = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player1, player2, player3)));
    }

    @Test
    void setState()throws IllegalArgumentException {
        Player player1 = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player1, player2, player3)));


    }
}