package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

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
    }

    @Test
    void getPlayerPositions() throws IllegalArgumentException{
    }

    @Test
    void putPlayerPosition() throws IllegalArgumentException {
    }

    @Test
    void getBoard() throws IllegalArgumentException{
    }

    @Test
    void getState() throws IllegalArgumentException {
    }

    @Test
    void setState()throws IllegalArgumentException {
    }
}