package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EffectDrawTest {

    @Test
    void applyEffectDraw() {
        //D1
        Player player = new Player("Elisa");
        Artist a = new Artist(Era.I);
        Builder b = new Builder(2,2,Era.I);
        Gatherer g = new Gatherer(Era.I);
        Hunter h = new Hunter(true, Era.I);
        Inventor inv = new Inventor(Icon.ARROW, Era.I);
        Shaman s = new Shaman(2, Era.I);

        Artist a2 = new Artist(Era.I);
        Builder b2 = new Builder(2,2,Era.I);
        Gatherer g2 = new Gatherer(Era.I);
        Hunter h2 = new Hunter(true, Era.I);
        Inventor inv2 = new Inventor(Icon.ARROW, Era.I);
        Shaman s2 = new Shaman(2, Era.I);

        EffectDraw ed = new EffectDraw(2, IDEffect.D1);

        assertEquals(0, player.getFood());
        assertEquals(0, player.countSets());
        ed.applyEffectDraw(player);
        assertEquals(0, player.getFood());

        player.addCard(a);
        player.addCard(b);
        player.addCard(g);
        player.addCard(h);
        player.addCard(inv);
        player.addCard(s);

        ed.applyEffectDraw(player);
        assertEquals(2, player.getFood());

        player.addCard(s2);
        player.addCard(a2);
        player.addCard(b2);
        player.addCard(g2);
        player.addCard(h2);
        player.addCard(inv2);

        ed.applyEffectDraw(player);
        assertEquals(4, player.getFood());

        //D2

        EffectDraw ed2 = new EffectDraw(3, IDEffect.D2);
        Inventor inv3 = new Inventor(Icon.LEATHER, Era.I);
        Inventor inv4 = new Inventor(Icon.LEATHER, Era.I);
        Inventor inv5 = new Inventor(Icon.SOUP, Era.I);
        player.addCard(inv3);
        player.addCard(inv5);

        ed2.applyEffectDraw(player);
        assertEquals(4, player.getFood());

        player.addCard(inv4);
        assertEquals(7, player.getFood());
    }

    @Test
    void getFood() {
        EffectDraw effect = new EffectDraw(3, IDEffect.D1);
        assertEquals(3, effect.getFood());
    }
}