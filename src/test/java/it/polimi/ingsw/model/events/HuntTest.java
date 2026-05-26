package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.Hunter;
import it.polimi.ingsw.model.effects.Effect;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HuntTest {

    @Test
    void applyEffect() {
        Hunt h = new Hunt(1, Era.I);
        Player p1 = new Player("Elisa");
        p1.setFood(0);
        p1.setPp(0);
        List<Player> players = new ArrayList<Player>();
        players.add(p1);
        Hunter h1 = new Hunter(false, Era.I);
        Hunter h2 = new Hunter(false, Era.I);
        Hunter h3 = new Hunter(false, Era.I);
        Hunter h4 = new Hunter(false, Era.I);

        //No hunters in the tribe
        h.applyEffect(players);
        assertEquals(0, p1.getFood());
        assertEquals(0, p1.getPp());

        //1 hunter in the tribe
        try {
            p1.addCard(h1);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }
        h.applyEffect(players);
        assertEquals(1, p1.getFood());
        assertEquals(1, p1.getPp());

        //4 hunters in the tribe
        try {
            p1.addCard(h2);
            p1.addCard(h3);
            p1.addCard(h4);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }
        h.applyEffect(players);
        assertEquals(5, p1.getFood());
        assertEquals(5, p1.getPp());
        //0 cacciatori
        //x cacciatori

        //With building effect
        p1.setPp(0);
        try {
            p1.addCard(new Building(Era.I, 2, 2, 0, null, null, Effect.EH));
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }
        h.applyEffect(players);
        assertEquals(13, p1.getFood());
        assertEquals(8, p1.getPp());
    }

    @Test
    void getPp() {
        Hunt h = new Hunt(5, Era.I);
        assertEquals(5, h.getPp());
    }

    @Test
    void getName() {
        Hunt h = new Hunt(5, Era.I);
        assertEquals("Hunt", h.getName());
    }
}