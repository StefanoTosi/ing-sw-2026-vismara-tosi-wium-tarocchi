package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.Artist;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CavePaintingsTest {

    @Test
    void applyeffect() {
        //easy add
        Player p1 = new Player("Elisa");
        Player p2 = new Player("Lisa");
        Player p3 = new Player("Gilles");
        Player p4 = new Player("Ste");

        CavePaintings ef = new CavePaintings(2, Era.I);

        Artist a1 = new Artist(Era.I);
        Artist a2 = new Artist(Era.I);
        Artist a3 = new Artist(Era.I);
        Artist a4 = new Artist(Era.I);
    }

    @Test
    void getName() {
    }
}