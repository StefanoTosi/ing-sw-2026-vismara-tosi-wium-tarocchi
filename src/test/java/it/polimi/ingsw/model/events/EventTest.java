package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EventTest {

    @Test
    void addToPlayer() {
        Event ef = new CavePaintings(2, Era.I, 4, 5);
        Player p1 = new Player("Lisa");

        IllegalActionException ex = assertThrows(IllegalActionException.class, () -> {
            ef.addToPlayer(p1);
        });

        assertEquals("Player tried to draw an event", ex.getReason());
    }
}
