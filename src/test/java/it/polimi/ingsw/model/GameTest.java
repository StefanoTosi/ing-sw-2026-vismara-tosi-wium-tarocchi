package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.characters.Artist;
import it.polimi.ingsw.model.FakeRMIObserver;
import it.polimi.ingsw.model.FakeTCPObserver;
import it.polimi.ingsw.model.events.EventResult;
import it.polimi.ingsw.networking.RMI.ClientCallBack;
import it.polimi.ingsw.networking.RMI.ClientRMI;
import org.junit.jupiter.api.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

import static javax.management.Query.times;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    void Game () throws IllegalArgumentException {
        Player p = new Player("A");
        List<Player> players = List.of(p);
        int numPlayers = 1;
        Board board = new Board(new Random(42));
        List<Player> rankings = List.of(p);
        Player playerTurn = p;
        String errorFlag = "test error";
        int turnNumber = 5;
        List<List<EventResult>> eventResults = null;

        Game g = new Game(players, numPlayers, board, rankings, playerTurn, errorFlag, turnNumber, eventResults);

        assertEquals(players,  g.getPlayers());
        assertEquals(numPlayers, g.getNumPlayers());
        assertEquals(board, g.getBoard());
        assertEquals(rankings, g.getRankings());
        assertEquals(playerTurn, g.getPlayerTurn());
        assertEquals(errorFlag, g.getErrorFlag());
        assertEquals(turnNumber, g.getTurnNumber());
        assertEquals(eventResults, g.getEventResults());
    }

    @Test
    void getNumPlayers() throws IllegalArgumentException {
        Player player1 = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player1, player2, player3)), null);
        assertEquals(0, game.getNumPlayers());

        game.setNumPlayers(3);
        assertEquals(3, game.getNumPlayers());
    }

    @Test
    void getPlayers() throws IllegalArgumentException{
        Player player1 = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player1, player2, player3)), null);
        List<Player> test = Arrays.asList(player1, player2, player3);
        assert game.getPlayers().equals(test);
    }

    @Test
    void getPlayerPositions() throws IllegalArgumentException{
        Player player1 = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player1, player2, player3)), null);
    }

    @Test
    void putPlayerPosition() throws IllegalArgumentException {
        Player player1 = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player1, player2, player3)), null);
    }

    @Test
    void getBoard() throws IllegalArgumentException{
        Player player1 = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player1, player2, player3)), new Random(42));
        Board board = new Board(new Random(42));
        board.initialize(3);
        Card card = new Artist(Era.II, 0);
        Card card2 = new Artist(Era.II, 0);
        Card card3 = new Artist(Era.II, 0);
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
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player1, player2, player3)), null);
    }

    @Test
    void setState()throws IllegalArgumentException {
        Player player1 = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player1, player2, player3)), null);
    }

    @Test
    void notifyRMIObservers() {
        Player p1 = new Player("A");
        Game game = new Game(List.of(p1), new Random(1));
        game.setNumPlayers(1);

        FakeRMIObserver observer = new FakeRMIObserver();
        game.addObserverRMI(observer);

        game.notifyObserver();

        assertEquals(1, observer.updateCalls);
    }

    @Test
    void notifyTCPObservers() {
        Player p1 = new Player("A");
        Game game = new Game(List.of(p1), new Random(1));
        game.setNumPlayers(1);

        FakeTCPObserver observer = new FakeTCPObserver();
        game.addObserverTCP(observer);

        game.notifyObserver();

        assertEquals(1, observer.updateCalls);
    }

    @Test
    void removeObserverRMI() {
        Player p1 = new Player("A");
        Game game = new Game(List.of(p1), new Random(1));
        game.setNumPlayers(1);

        FakeRMIObserver observer = new FakeRMIObserver();

        game.addObserverRMI(observer);
        game.removeObserverRMI(observer);

        game.notifyObserver();

        assertEquals(0, observer.updateCalls);
    }

    @Test
    void removeObserverTCP() {
        Player p1 = new Player("A");
        Game game = new Game(List.of(p1), new Random(1));
        game.setNumPlayers(1);

        FakeTCPObserver observer = new FakeTCPObserver();

        game.addObserverTCP(observer);
        game.removeObserverTCP(observer);

        game.notifyObserver();

        assertEquals(0, observer.updateCalls);
    }

    @Test
    void closingGame() {
        Player p1 = new Player("A");
        Game game = new Game(List.of(p1), new Random(1));

        FakeRMIObserver rmi = new FakeRMIObserver();
        FakeTCPObserver tcp = new FakeTCPObserver();

        game.addObserverRMI(rmi);
        game.addObserverTCP(tcp);

        game.closingGame();

        assertEquals(1, rmi.closingCalls);
        assertEquals(1, tcp.closingCalls);
    }

    @Test
    void canResume() {
        Player p1 = new Player("A");
        Game game = new Game(List.of(p1), new Random(1));
        game.setNumPlayers(2);

        FakeRMIObserver rmi = new FakeRMIObserver();
        FakeTCPObserver tcp = new FakeTCPObserver();

        game.addObserverRMI(rmi);
        game.addObserverTCP(tcp);

        game.canResume();

        assertEquals(1, rmi.updateCalls);
        assertEquals(1, tcp.updateCalls);
    }

    //If the update crash for any reasone, it doesn't stop the loop
    @Test
    void crashResilience() {
        Player p1 = new Player("A");
        Game game = new Game(List.of(p1), new Random(1));

        ClientCallBack failing = new ClientCallBack() {
            @Override
            public void receiveMessage(String message) throws RemoteException {
            }

            @Override
            public void setNickname(String nickname) throws RemoteException {
            }

            @Override
            public String getNickname() throws RemoteException {
                return "";
            }

            @Override
            public void update(GameDTO game) {
                throw new RuntimeException();
            }

            @Override
            public void closingGame(GameDTO game) {}
        };

        game.addObserverRMI(failing);

        assertDoesNotThrow(() -> game.notifyObserver());
    }

    @Test
    void multipleObservers() {
        Player p1 = new Player("A");
        Game game = new Game(List.of(p1), new Random(1));

        FakeRMIObserver rmi = new FakeRMIObserver();
        FakeTCPObserver tcp = new FakeTCPObserver();

        game.addObserverRMI(rmi);
        game.addObserverTCP(tcp);

        game.notifyObserver();

        assertEquals(1, rmi.updateCalls);
        assertEquals(1, tcp.updateCalls);
    }

    @Test
    void newTurn() {
        Player p1 = new Player("A");
        Game game = new Game(List.of(p1), new Random(1));

        game.newTurn();
        assertEquals(2, game.getTurnNumber());
    }
}