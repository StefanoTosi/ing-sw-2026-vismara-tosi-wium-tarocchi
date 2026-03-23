package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.Artist;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CavePaintingsTest {

    @Test
    void applyeffect() {
        Player p1 = new Player("Elisa");
        p1.setPp(20);

        CavePaintings ef = new CavePaintings(2, Era.I, 5, 4);

        Artist a1 = new Artist(Era.I);
        Artist a2 = new Artist(Era.I);
        Artist a3 = new Artist(Era.I);

        ef.applyeffect(p1);
        assertEquals(16, p1.getPp());

        p1.addCard(a1);

        ef.applyeffect(p1);
        assertEquals(12, p1.getPp());

        p1.addCard(a2);
        ef.applyeffect(p1);
        assertEquals(17, p1.getPp());

        p1.addCard(a3);
        ef.applyeffect(p1);
        assertEquals(22, p1.getPp());
    }

    @Test
    void getName() {
        CavePaintings ef = new CavePaintings(2, Era.I, 5, 4);
        assertEquals("CavePaintings", ef.getName());
    }
}