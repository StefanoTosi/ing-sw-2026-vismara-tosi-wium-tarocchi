package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HunterTest {

    @Test
    void getIcon() {
        Hunter h = new Hunter(true, Era.I);

        assertEquals(true, h.getIcon());
    }

    @Test
    void addToPlayer() {
        Player p = new Player("Gilles");
        Hunter h = new Hunter(true, Era.I);

        assertEquals(0, p.getHunters().size());
        h.addToPlayer(p);
        assertEquals(1, p.getHunters().size());
        assertEquals(h, p.getHunters().get(0));
    }
}