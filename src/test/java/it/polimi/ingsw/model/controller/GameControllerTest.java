package it.polimi.ingsw.model.controller;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.actions.ChooseTotemAction;
import it.polimi.ingsw.controller.actions.DrawCardFromBottomAction;
import it.polimi.ingsw.controller.actions.DrawCardFromTopAction;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.RMI.ClientCallBack;
import it.polimi.ingsw.networking.TCP.ObserverTCP;
import it.polimi.ingsw.networking.User;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    @Test
    void setterGetterGames() throws IllegalActionException, IOException, ClassNotFoundException, InterruptedException {
        GameController gameController = new GameController();
        Player p1 = new Player("Elisa");
        Player p2 = new Player("Stefano");
        Player p3 = new Player("Gilles");
        Game g1 = new Game(new ArrayList<Player>(Arrays.asList(p1, p2, p3)), new Random(42));
        p1.setGame(g1);
        p2.setGame(g1);
        p3.setGame(g1);

        gameController.setGames(new ArrayList<Game>(Arrays.asList(g1)));
        assertEquals(1, gameController.getGames().size());

        Player p4 = new Player("a");
        Player p5 = new Player("b");
        Player p6 = new Player("c");
        Game g2 = new Game(new ArrayList<Player>(Arrays.asList(p4, p5, p6)), new Random(42));
        p4.setGame(g2);
        p5.setGame(g2);
        p6.setGame(g2);

        gameController.addGame(g2);

        assertEquals(2, gameController.getGames().size());

        gameController.removeGame("Elisa");
        assertEquals(1, gameController.getGames().size());
    }

    @Test
    void getPlayer(){
        GameController gameController = new GameController();
        Player p1 = new Player("Elisa");
        Player p2 = new Player("Stefano");
        Player p3 = new Player("Gilles");
        Game g1 = new Game(new ArrayList<Player>(Arrays.asList(p1, p2, p3)), new Random(42));

        Player p4 = new Player("a");
        Player p5 = new Player("b");
        Player p6 = new Player("c");
        Game g2 = new Game(new ArrayList<Player>(Arrays.asList(p4, p5, p6)), new Random(42));

        gameController.setGames(new ArrayList<Game>(Arrays.asList(g1, g2)));

        assertEquals(p1, gameController.getPlayer("Elisa"));
    }

    @Test
    void createGameRMI() throws IllegalActionException, IOException {
        GameController gameController = new GameController();
        Player p1 = new Player("A");
        FakeRMIObserver o1 = new FakeRMIObserver();
        gameController.createGameRMI(p1, 3, o1);

        Player p2 = new Player("B");
        FakeRMIObserver o2 = new FakeRMIObserver();

        assertTrue(gameController.joinGameRMI(p2, o2));
        assertEquals(1, o2.updateCalls);

        Player p3 = new Player("B");
        FakeRMIObserver o3 = new FakeRMIObserver();

        assertTrue(gameController.joinGameRMI(p2, o2));
        assertEquals(3, o2.updateCalls);
        assertEquals(2, o1.updateCalls);
        assertEquals(0, o3.updateCalls);
    }

    @Test
    void createGameTCP() throws Exception {
        GameController gameController = new GameController();
        Player p1 = new Player("A");
        FakeTCPObserver o1 = new FakeTCPObserver();
        gameController.createGameTCP(p1, 3, o1);

        Player p2 = new Player("B");
        FakeTCPObserver o2 = new FakeTCPObserver();

        assertTrue(gameController.joinGameTCP(p2, o2));
        assertEquals(1, o2.updateCalls);

        Player p3 = new Player("C");
        FakeTCPObserver o3 = new FakeTCPObserver();

        assertTrue(gameController.joinGameTCP(p3, o3));
        assertEquals(2, o2.updateCalls);
        assertEquals(2, o1.updateCalls);
        assertEquals(1, o3.updateCalls);
    }

    @Test
    void leaveReconnectTCP() throws Exception {
        GameController gameController = new GameController();
        Player p1 = new Player("A");
        FakeTCPObserver o1 = new FakeTCPObserver();
        gameController.createGameTCP(p1, 3, o1);

        Player p2 = new Player("B");
        FakeTCPObserver o2 = new FakeTCPObserver();

        assertTrue(gameController.joinGameTCP(p2, o2));
        assertEquals(1, o2.updateCalls);

        Player p3 = new Player("C");
        FakeTCPObserver o3 = new FakeTCPObserver();

        assertTrue(gameController.joinGameTCP(p3, o3));
        gameController.leaveMatchTCP("A", o1);
        gameController.leaveMatchTCP("B", o2);

        gameController.reconnectGameTCP("A", o1);
        assertEquals(2, o2.updateCalls);
        assertEquals(2, o1.updateCalls);
        assertEquals(1, o3.updateCalls);

        gameController.reconnectGameTCP("B", o2);
        assertEquals(3, o2.updateCalls);
        assertEquals(3, o1.updateCalls);
        assertEquals(2, o3.updateCalls);
    }

    @Test
    void leaveReconnectRMI() throws Exception {
        GameController gameController = new GameController();
        Player p1 = new Player("A");
        FakeRMIObserver o1 = new FakeRMIObserver();
        gameController.createGameRMI(p1, 3, o1);

        Player p2 = new Player("B");
        FakeRMIObserver o2 = new FakeRMIObserver();

        assertTrue(gameController.joinGameRMI(p2, o2));
        assertEquals(1, o2.updateCalls);

        Player p3 = new Player("C");
        FakeRMIObserver o3 = new FakeRMIObserver();

        assertTrue(gameController.joinGameRMI(p3, o3));
        gameController.leaveMatchRMI("A", o1);
        gameController.leaveMatchRMI("B", o2);

        gameController.reconnectGameRMI("A", o1);
        assertEquals(2, o2.updateCalls);
        assertEquals(2, o1.updateCalls);
        assertEquals(1, o3.updateCalls);

        gameController.reconnectGameRMI("B", o2);
        assertEquals(3, o2.updateCalls);
        assertEquals(3, o1.updateCalls);
        assertEquals(2, o3.updateCalls);
    }

    @Test
    void executeAction() throws Exception {
        GameController gameController = new GameController();
        Player p1 = new Player("A");
        FakeTCPObserver o1 = new FakeTCPObserver();
        gameController.createGameTCP(p1, 3, o1);

        Player p2 = new Player("B");
        FakeTCPObserver o2 = new FakeTCPObserver();

        gameController.joinGameTCP(p2, o2);

        Player p3 = new Player("C");
        FakeTCPObserver o3 = new FakeTCPObserver();

        gameController.joinGameTCP(p3, o3);

        assertThrows(IllegalActionException.class, () -> {
            gameController.executeAction(new DrawCardFromTopAction(0), "A");
        });

        assertEquals(3, o2.updateCalls);
        assertEquals(3, o1.updateCalls);
        assertEquals(2, o3.updateCalls);

        assertThrows(IllegalActionException.class, () -> {
            gameController.executeAction(new DrawCardFromTopAction(0), "B");
        });

        assertEquals(4, o2.updateCalls);
        assertEquals(4, o1.updateCalls);
        assertEquals(3, o3.updateCalls);

    }

}
