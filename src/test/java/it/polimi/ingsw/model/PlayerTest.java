package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    // per tutti gli add controlla che non venga passato un null

    @Test
    void getName() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");

        assertEquals("Elisa", player.getName());
    }

    @Test
    void getPp() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        player.setPp(42);
        assertEquals(42, player.getPp());
    }

    @Test
    void setPp() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        player.setPp(42);
        assertEquals(42, player.getPp());
    }

    @Test
    void addPp() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        player.addPp(42);
        assertEquals(42, player.getPp());
        player.addPp(-3);
        assertEquals(39, player.getPp());
        player.addPp(-40);
        assertEquals(-1, player.getPp());
    }

    @Test
    void getFood() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        player.setFood(42);
        assertEquals(42, player.getFood());
    }

    @Test
    void setFood() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        player.setFood(42);
        assertEquals(42, player.getFood());
        assertThrows(IllegalArgumentException.class, ()->player.setFood(-4));
    }


    @Test
    void addFood() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        player.addFood(42);
        assertEquals(42, player.getFood());
        assertThrows(IllegalArgumentException.class, ()->player.addFood(-43));
    }

    // prova a mettere una carta di ciascun tipo e provi a vedere se viene aggiunta alla lista,
    // se viene passato un puntatore nullo
    // ad addCard non andrebbe mai passato un Evento
    @Test
    void addCard() {
    }

    // verifica che aggiunge
    @Test
    void addArtist() {
    }

    @Test
    void addGatherer() {
    }

    @Test
    void addHunter() {
    }

    @Test
    void addInventor() {
    }

    @Test
    void addShaman() {
    }

    @Test
    void addBuilder() {
    }

    // verifica aggiungendo set che il numero si modifichi - prima 0 - poi di tutte almeno 2 e di una 1, e viceversa
    @Test
    void countSets() {
    }

    // verifica che non prenda altre carte
    // verifica che aggiunga
    // aggiungere un building con ogni effetto
    @Test
    void addBuilding() {
    }

    @Test
    void countNumCharacters() {
    }

    @Test
    void countNumBuildings() {
    }

    @Test
    void getNumArtists() {
    }

    @Test
    void getNumGatherers() {
    }

    @Test
    void getNumInventors() {
    }

    @Test
    void getNumHunters() {
    }

    @Test
    void getNumShamans() {
    }

    @Test
    void getNumBuilders() {
    }

    @Test
    void getNumStars() {
    }

    @Test
    void countBuildersPp() {
    }

    @Test
    void countTribePp() {
    }

    @Test
    void getGame() {
    }
}