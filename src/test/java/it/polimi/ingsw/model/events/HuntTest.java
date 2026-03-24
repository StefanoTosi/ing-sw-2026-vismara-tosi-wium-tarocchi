package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Building;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.Hunter;
import it.polimi.ingsw.model.effects.Effect;
import it.polimi.ingsw.model.effects.EffectDraw;
import it.polimi.ingsw.model.effects.EffectEvent;
import it.polimi.ingsw.model.effects.IDEffect;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HuntTest {

    @Test
    void applyEffect() {
        Hunt h = new Hunt(1, Era.I);
        Player p1 = new Player("Elisa");
        Hunter h1 = new Hunter(true, Era.I);
        Hunter h2 = new Hunter(true, Era.I);
        Hunter h3 = new Hunter(true, Era.I);
        Hunter h4 = new Hunter(true, Era.I);

        h.applyEffect(p1);
        assertEquals(0, p1.getFood());
        assertEquals(0, p1.getPp());

        p1.addCard(h1);
        h.applyEffect(p1);
        assertEquals(1, p1.getFood());
        assertEquals(1, p1.getPp());

        p1.addCard(h2);
        p1.addCard(h3);
        p1.addCard(h4);
        h.applyEffect(p1);
        assertEquals(5, p1.getFood());
        assertEquals(5, p1.getPp());
        //0 cacciatori
        //x cacciatori
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