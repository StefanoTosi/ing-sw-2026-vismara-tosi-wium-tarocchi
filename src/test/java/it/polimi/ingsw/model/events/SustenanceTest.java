package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.Artist;
import it.polimi.ingsw.model.characters.Gatherer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SustenanceTest {

    @Test
    void applyeffect() {
        //pagamento senza raccoglitori
        //pagamento con x raccoglitori
        //riesco a pagare tutto
        //non riesco a pagare tutto
        //combinazioni varie
        Sustenance s = new Sustenance(5, Era.I);
        Gatherer g = new Gatherer(Era.I);
        Artist a1 = new Artist(Era.I);
        Artist a2 = new Artist(Era.I);
        Artist a3 = new Artist(Era.I);
        Artist a4 = new Artist(Era.I);

        Player p1 = new Player("Elisa");
        Player p2 = new Player("Lisa");
        Player p3 = new Player("Gilles");
        Player p4 = new Player("Stefano");
        List<Player> players = new ArrayList<>(Arrays.asList(p1, p2, p3, p4));

        //pago tutto senza gatherer
        p1.setFood(5);
        p1.setPp(10);
        p1.addCard(a1);

        assertEquals(10, p1.getPp());
        assertEquals(5, p1.getFood());

        //non riesco senza gatherer
        p2.setFood(1);
        p2.setPp(10);
        p2.addCard(a1);
        p2.addCard(a2);
        p2.addCard(a3);

        assertEquals(0, p2.getPp());
        assertEquals(0, p2.getFood());

        //riesco con gatherer
        p3.setFood(5);
        p3.setPp(10);
        p3.addCard(a1);
        p3.addCard(g);

        assertEquals(10, p3.getPp());
        assertEquals(5, p3.getFood());

        //non riesco con gatherer
        p4.setFood(0);
        p4.setPp(10);
        p4.addCard(a1);
        p4.addCard(a2);
        p4.addCard(a3);
        p4.addCard(a4);
        p4.addCard(g);

        assertEquals(5, p4.getPp());
        assertEquals(0, p4.getFood());
    }

    @Test
    void getPp() {
        Sustenance s = new Sustenance(5, Era.I);
        assertEquals(5, s.getPp());
    }

    @Test
    void getName() {
        Sustenance s = new Sustenance(5, Era.I);
        assertEquals("Sustenance", s.getName());
    }
}