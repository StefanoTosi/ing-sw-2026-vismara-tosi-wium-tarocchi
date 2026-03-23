package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EffectEndGameTest {

    @Test
    void applyEffectEndGame() {
        //aggiunge pp a fine gioco
        //EG4
        Player player = new Player("Elisa");
        EffectEndGame eg = new EffectEndGame(25,IDEffect.EG4);
        eg.applyEffectEndGame(player);
        assertEquals(25, player.getPp());

        //EG2
        //aggiunge 6pp per set di personaggio
        //diversi numeri di set, deve ricevere il punteggio giusto
        //set: 0, 1,2
        Player player2 = new Player("Lisa");
        EffectEndGame eg2 = new EffectEndGame(25,IDEffect.EG4);

        eg2.applyEffectEndGame(player2);
        assertEquals(0, player2.getPp());

        Artist a = new Artist(Era.I);
        Builder b = new Builder(2,2,Era.I);
        Gatherer g = new Gatherer(Era.I);
        Hunter h = new Hunter(true, Era.I);
        Inventor inv = new Inventor(Icon.ARROW, Era.I);
        Shaman s = new Shaman(2, Era.I);

        player.addCard(a);
        player.addCard(b);
        player.addCard(g);
        player.addCard(inv);
        player.addCard(s);
        player.addCard(h);

        eg2.applyEffectEndGame(player2);
        assertEquals(6, player.getPp());

        Artist a1 = new Artist(Era.I);
        Builder b1 = new Builder(2,2,Era.I);
        Gatherer g1 = new Gatherer(Era.I);
        Hunter h1 = new Hunter(true, Era.I);
        Inventor inv1 = new Inventor(Icon.ARROW, Era.I);
        Shaman s1 = new Shaman(2, Era.I);

        player.addCard(a1);
        player.addCard(b1);
        player.addCard(g1);
        player.addCard(inv1);
        player.addCard(s1);
        player.addCard(h1);

        eg2.applyEffectEndGame(player2);
        assertEquals(12, player.getPp());

        Artist a2 = new Artist(Era.I);
        Builder b2 = new Builder(2,2,Era.I);
        Gatherer g2 = new Gatherer(Era.I);
        Hunter h2 = new Hunter(true, Era.I);
        Inventor inv2 = new Inventor(Icon.ARROW, Era.I);
        Shaman s2 = new Shaman(2, Era.I);

        player.addCard(a2);
        player.addCard(b2);
        player.addCard(g2);
        player.addCard(inv2);
        player.addCard(s2);
        player.addCard(h2);

        eg2.applyEffectEndGame(player2);
        assertEquals(18, player.getPp());

        //guadagno doppio dei pp su costruttori

        Player player3 = new Player("Gilles");
        EffectEndGame eg3 = new EffectEndGame(0,IDEffect.EG1);

        eg3.applyEffectEndGame(player3);
        assertEquals(0, player3.getPp());

        Builder b3 = new Builder(2,4,Era.I);
        player3.addCard(b3);
        eg3.applyEffectEndGame(player3);

        assertEquals(8, player3.getPp());

        //in base a personaggio rappresentato dall'effetto da pp rappresentati a fine gioco
        //per ogni carta di quel tipo
    }

    @Test
    void getPp() {
        EffectEndGame effect = new EffectEndGame(3, IDEffect.EG1);
        assertEquals(3, effect.getPp());
    }
}