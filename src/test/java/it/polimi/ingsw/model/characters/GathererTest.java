package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GathererTest {

    @Test
    void addToPlayer() {
        // Instantiate a player and a card
        Player p = new Player("Gilles");
        Gatherer g = new Gatherer(Era.I);

        // Check it gets added correctly
        assertEquals(0, p.getGatherers().size());
        g.addToPlayer(p);
        assertEquals(1, p.getGatherers().size());

        // Check it gets counted correctly
        assertEquals(g, p.getGatherers().get(0));
    }
}