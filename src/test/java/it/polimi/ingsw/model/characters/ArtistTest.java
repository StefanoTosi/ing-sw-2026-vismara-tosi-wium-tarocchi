package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArtistTest {

    @Test
    void addToPlayer() {
        Player p = new Player("Gilles");
        Artist a = new Artist(Era.I);

        assertEquals(0, p.getArtists().size());
        a.addToPlayer(p);
        assertEquals(1, p.getArtists().size());
        assertEquals(a, p.getArtists().get(0));
    }
}