package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.Artist;
import it.polimi.ingsw.model.characters.Gatherer;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.effects.Effect;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SustenanceTest {

    @Test
    void applyEffect() {
        Sustenance s = new Sustenance(5, Era.I);
        Gatherer g = new Gatherer(Era.I);
        Artist a1 = new Artist(Era.I);
        Artist a2 = new Artist(Era.I);
        Artist a3 = new Artist(Era.I);
        Artist a4 = new Artist(Era.I);

        // P1 is able to pay all the food
        Player p1 = new Player("Elisa");
        p1.setFood(5);
        p1.setPp(10);
        try {
            p1.addCard(a1);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        // P2 is unable to pay all the food
        Player p2 = new Player("Lisa");
        p2.setFood(1);
        p2.setPp(10);
        try {
            p2.addCard(a1);
            p2.addCard(a2);
            p2.addCard(a3);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        // P3 is able to pay with gatherers
        Player p3 = new Player("Gilles");
        p3.setFood(5);
        p3.setPp(10);
        try {
            p3.addCard(a1);
            p3.addCard(g);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        // P4 is unable to pay even with gatherers
        Player p4 = new Player("Stefano");
        p4.setFood(0);
        p4.setPp(10);
        try {
            p4.addCard(a1);
            p4.addCard(a2);
            p4.addCard(a3);
            p4.addCard(a4);
            p4.addCard(g);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        // P5 is able to pay thanks to the food discount from a building
        Player p5 = new Player("Tomasulo");
        p5.setFood(0);
        p5.setPp(10);
        Building b = new Building(Era.I, 1, 1, 0, (_) -> 4, null, Effect.ES1);
        try {
            p5.addCard(a1);
            p5.addCard(a2);
            p5.addCard(a3);
            p5.addCard(a4);
            p5.addCard(b);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        List<Player> players = Arrays.asList(p1, p2, p3, p4, p5);
        s.applyEffect(players);

        assertEquals(10, p1.getPp());
        assertEquals(4, p1.getFood());

        assertEquals(0, p2.getPp());
        assertEquals(0, p2.getFood());

        assertEquals(10, p3.getPp());
        assertEquals(5, p3.getFood());

        assertEquals(0, p4.getPp());
        assertEquals(0, p4.getFood());

        assertEquals(10, p5.getPp());
        assertEquals(0, p5.getFood());
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

    @Test
    void toDTO(){
        Sustenance s = new Sustenance(5, Era.I);

        //check manually
        for(StringBuilder line : s.toDTO().printCard()){
            System.out.println(line);
        }
    }
}