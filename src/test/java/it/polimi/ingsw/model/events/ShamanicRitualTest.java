package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.Shaman;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ShamanicRitualTest {

    @Test
    void applyEffect() {
        //player con piu' stelle guadagna
        //player con meno perde
        //se due con stesse stelle perdono o vincono entrambi
        //2 max stelle
        //2 min stelle
        //1 max e 1 min
        //2 max e 2 min
        //tutti con stesse stelle, prima tutti guad e poi tutti perdono
        //TODO: testare effetti (dontLosePp, doublePp, anche con pareggi) e pareggio di tutti i giocatori
        ShamanicRitual sr = new ShamanicRitual(5,-3, Era.I);

        Player p1 = new Player("Elisa");
        Player p2 = new Player("Lisa");
        Player p3 = new Player("Gilles");
        Shaman s1 = new Shaman(5, Era.I);
        Shaman s2 = new Shaman(2, Era.I);
        Shaman s3 = new Shaman(1, Era.I);

        try {
            p1.addCard(s1);
            p2.addCard(s1);
            p3.addCard(s2);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        sr.applyEffect(new ArrayList<Player>(Arrays.asList(p1, p2, p3)));
        assertEquals(5, p1.getPp());
        assertEquals(5, p2.getPp());
        assertEquals(-3, p3.getPp());

        Player p4 = new Player("Elisa");
        Player p5 = new Player("Lisa");
        Player p6 = new Player("Gilles");

        try {
            p4.addCard(s2);
            p5.addCard(s2);
            p6.addCard(s1);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        sr.applyEffect(new ArrayList<Player>(Arrays.asList(p4, p5, p6)));

        assertEquals(-3, p4.getPp());
        assertEquals(-3, p5.getPp());
        assertEquals(5, p6.getPp());

        Player p7 = new Player("Elisa");
        Player p8 = new Player("Lisa");
        Player p9 = new Player("Gilles");

        try {
            p7.addCard(s1);
            p8.addCard(s2);
            p9.addCard(s3);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        sr.applyEffect(new ArrayList<Player>(Arrays.asList(p7, p8, p9)));

        assertEquals(5, p7.getPp());
        assertEquals(0, p8.getPp());
        assertEquals(-3, p9.getPp());

        Player p10 = new Player("Elisa");
        Player p11 = new Player("Lisa");
        Player p12 = new Player("Gilles");
        Player p13 = new Player("Gilles");

        try {
            p10.addCard(s2);
            p11.addCard(s2);
            p12.addCard(s1);
            p13.addCard(s1);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        sr.applyEffect(new ArrayList<Player>(Arrays.asList(p10, p11, p12, p13)));

        assertEquals(-3, p10.getPp());
        assertEquals(-3, p11.getPp());
        assertEquals(5, p12.getPp());
        assertEquals(5, p13.getPp());

        Player p14 = new Player("Elisa");
        Player p15 = new Player("Lisa");

        try {
            p14.addCard(s1);
            p15.addCard(s1);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        sr.applyEffect(new ArrayList<Player>(Arrays.asList(p14, p15)));

        assertEquals(2, p14.getPp());
        assertEquals(2, p15.getPp());
    }

    @Test
    void getEra() {
        ShamanicRitual sr = new ShamanicRitual(5,3, Era.I);
        assertEquals(Era.I,sr.getEra());
    }

    @Test
    void getName() {
        ShamanicRitual sr = new ShamanicRitual(5,3, Era.I);
        assertEquals("ShamanicRitual",sr.getName());
    }
}