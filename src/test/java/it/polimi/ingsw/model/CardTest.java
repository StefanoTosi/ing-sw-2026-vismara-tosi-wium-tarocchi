package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characters.Artist;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardTest {

    @Test
    void testCard(){
        Card c = new Artist(Era.I);
        assertEquals(Era.I, c.getEra());
        assertEquals("Artist", c.getName());
        assertEquals("Character", c.getType());

        c.setId(4);
        assertEquals(4, c.getId());
        assertEquals("Artist", c.toDTO().getName());
    }
}
