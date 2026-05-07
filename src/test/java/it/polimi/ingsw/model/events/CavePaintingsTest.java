package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.Artist;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.effects.Effect;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CavePaintingsTest {

    @Test
    void applyEffect() {
        Player p1 = new Player("Elisa");
        List<Player> players = new ArrayList<>();
        players.add(p1);
        p1.setPp(20);
        p1.setFood(0);

        CavePaintings ef = new CavePaintings(2, Era.I, 4, 5);

        Artist a1 = new Artist(Era.I);
        Artist a2 = new Artist(Era.I);
        Artist a3 = new Artist(Era.I);

        ef.applyEffect(players);
        assertEquals(16, p1.getPp());

        p1.addCard(a1);

        ef.applyEffect(players);
        assertEquals(12, p1.getPp());

        p1.addCard(a2);
        ef.applyEffect(players);
        assertEquals(17, p1.getPp());

        p1.addCard(a3);
        ef.applyEffect(players);
        assertEquals(22, p1.getPp());

        //Test building effect
        Building building = new Building(Era.I, 2, 3, 0, null, Effect.ECP);
        p1.addCard(building);
        ef.applyEffect(players);
        assertEquals(3, p1.getFood());
        p1.addCard(a1);
        ef.applyEffect(players);
        assertEquals(7, p1.getFood());
    }

    @Test
    void getName() {
        CavePaintings ef = new CavePaintings(2, Era.I, 5, 4);
        assertEquals("CavePaintings", ef.getName());
    }
}