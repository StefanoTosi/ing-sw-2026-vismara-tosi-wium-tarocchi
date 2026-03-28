package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShamanTest {

    @Test
    void getStars() {
        Shaman s = new Shaman(42, Era.I);

        assertEquals(42, s.getStars());
    }

    @Test
    void addToPlayer() {
        Player p = new Player("Gilles");
        Shaman s = new Shaman(42, Era.I);

        assertEquals(0, p.getShamans().size());
        s.addToPlayer(p);
        assertEquals(1, p.getShamans().size());
        assertEquals(s, p.getShamans().get(0));
    }
}