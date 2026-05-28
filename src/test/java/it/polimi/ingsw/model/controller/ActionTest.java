package it.polimi.ingsw.model.controller;

import it.polimi.ingsw.controller.actions.*;
import it.polimi.ingsw.controller.states.ChooseTotemState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ActionTest {
    @Test
    void chooseOffer(){
        Action a = new ChooseOfferAction('a');
        Player p = new Player("A");
        Game g = new Game(new ArrayList<Player>(Arrays.asList(p)), new Random(42));
        p.setGame(g);

        assertThrows(IllegalActionException.class, () -> {
            a.execute(p);
        });

        ChooseOfferAction b = new ChooseOfferAction('a');
        assertEquals('a', b.getOrder());
    }

    @Test
    void chooseTotem(){
        Action a = new ChooseTotemAction(Totem.BLUE);
        Player p = new Player("A");
        Game g = new Game(new ArrayList<Player>(Arrays.asList(p)), new Random(42));
        p.setGame(g);

        assertThrows(IllegalActionException.class, () -> {
            a.execute(p);
        });

        ChooseTotemAction b = new ChooseTotemAction(Totem.BLUE);
        assertEquals(Totem.BLUE, b.getTotem());
    }

    @Test
    void drawCardFromTop(){
        Action a = new DrawCardFromTopAction(0);
        Player p = new Player("A");
        Game g = new Game(new ArrayList<Player>(Arrays.asList(p)), new Random(42));
        p.setGame(g);

        assertThrows(IllegalActionException.class, () -> {
            a.execute(p);
        });

        DrawCardFromTopAction b = new DrawCardFromTopAction(0);
        assertEquals(0, b.getPos());
    }

    @Test
    void drawCardFromBottom(){
        Action a = new DrawCardFromBottomAction(0);
        Player p = new Player("A");
        Game g = new Game(new ArrayList<Player>(Arrays.asList(p)), new Random(42));
        p.setGame(g);

        assertThrows(IllegalActionException.class, () -> {
            a.execute(p);
        });

        DrawCardFromBottomAction b = new DrawCardFromBottomAction(0);
        assertEquals(0, b.getPos());
    }

    @Test
    void registerPlayer() throws IllegalActionException, IOException {
        Action a = new RegisterPlayerAction("A");
        Player p = new Player("A");
        Game g = new Game(new ArrayList<Player>(Arrays.asList()), new Random(42));
        p.setGame(g);

        a.execute(p);

        assertEquals(1, g.getPlayers().size());
    }

    @Test
    void startGame() throws IllegalActionException, IOException {
        Action a = new StartGameAction();
        Player p1 = new Player("A");
        Player p2 = new Player("A");
        Player p3 = new Player("A");
        Game g = new Game(new ArrayList<Player>(Arrays.asList(p1, p2, p3)), new Random(42));
        g.setNumPlayers(3);
        p1.setGame(g);
        p2.setGame(g);
        p3.setGame(g);

        a.execute(p1);

        assertTrue(g.getState() instanceof ChooseTotemState);
    }
}
