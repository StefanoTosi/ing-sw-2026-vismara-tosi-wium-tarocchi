package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.effects.Effect;
import it.polimi.ingsw.model.effects.EffectDraw;
import it.polimi.ingsw.model.effects.IDEffect;
import it.polimi.ingsw.model.events.Event;
import it.polimi.ingsw.model.events.ShamanicRitual;
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
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        Card card = null;
        player.addCard(card);
        assertEquals(0,player.countNumBuildings());
        assertEquals(0, player.countNumCharacters());
        Effect effect = new EffectDraw(1, IDEffect.D1);
        card = new Building(2,2, effect, Era.II);
        player.addCard(card);
        assertEquals(1,player.countNumBuildings());
        Card card3 = new Building(2,2, effect, Era.II);
        player.addCard(card3);
        assertEquals(2,player.countNumBuildings());
        Card card1 = new Artist(Era.II);
        player.addCard(card1);
        Card card2 = new ShamanicRitual(2, 2, Era.II);
        player.addCard(card2);
        assertEquals(1, player.countNumCharacters());
        Card card4 = new Artist(Era.II);
        player.addCard(card4);
        assertEquals(2, player.countNumCharacters());
    }

    // verifica che aggiunge
    @Test
    void addArtist() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        Artist card = new Artist(Era.II);
        player.addArtist(card);
        assertEquals(1, player.getNumArtists());
    }

    @Test
    void addGatherer() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        Gatherer card = new Gatherer(Era.II);
        player.addGatherer(card);
        assertEquals(1, player.getNumGatherers());
    }

    @Test
    void addHunter() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        Hunter card = new Hunter(true, Era.I);
        player.addHunter(card);
        assertEquals(1, player.getNumHunters());
    }

    @Test
    void addInventor() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        Inventor card = new Inventor(Icon.ARROW, Era.I);
        player.addInventor(card);
        assertEquals(1, player.getNumInventors());
    }

    @Test
    void addShaman() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        Shaman card = new Shaman(2, Era.II);
        player.addShaman(card);
        assertEquals(1, player.getNumShamans());
    }

    @Test
    void addBuilder() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        Builder card = new Builder(1, 2, Era.I);
        player.addBuilder(card);
        assertEquals(1, player.getNumBuilders());
    }

    // verifica aggiungendo set che il numero si modifichi - prima 0 - poi di tutte almeno 2 e di una 1, e viceversa
    @Test
    void countSets() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        Artist artist = new Artist(Era.II);
        player.addArtist(artist);
        Inventor inventor = new Inventor(Icon.ARROW, Era.I);
        player.addInventor(inventor);
        assertEquals(0, player.countSets());
        Hunter hunter = new Hunter(true, Era.I);
        player.addHunter(hunter);
        Shaman shaman = new Shaman(2, Era.II);
        player.addShaman(shaman);
        Gatherer gatherer = new Gatherer(Era.II);
        player.addGatherer(gatherer);
        Builder builder = new Builder(1, 2, Era.I);
        player.addBuilder(builder);
        assertEquals(1, player.countSets());
        Artist artist2 = new Artist(Era.II);
        player.addArtist(artist2);
        Inventor inventor2 = new Inventor(Icon.ARROW, Era.I);
        player.addInventor(inventor2);
        Hunter hunter2 = new Hunter(true, Era.I);
        player.addHunter(hunter2);
        Shaman shaman2 = new Shaman(2, Era.II);
        player.addShaman(shaman2);
        Gatherer gatherer2 = new Gatherer(Era.II);
        player.addGatherer(gatherer2);
        assertEquals(1, player.countSets());
        Builder builder2 = new Builder(1, 2, Era.I);
        player.addBuilder(builder2);
        assertEquals(2, player.countSets());
    }

    // verifica che non prenda altre carte
    // verifica che aggiunga
    // aggiungere un building con ogni effetto
    @Test
    void addBuilding() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        assertEquals(0, player.countNumBuildings());
        Effect effect = new EffectDraw(2, IDEffect.D1);
        Building building = new Building(2, 3,effect, Era.II);
        building.era = Era.I;
        player.addBuilding(building);
        assertEquals(1, player.countNumBuildings());

    }

    @Test
    void countNumCharacters() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        assertEquals(0, player.countNumCharacters());
        Artist card = new Artist(Era.II);
        player.addArtist(card);
        Inventor card2 = new Inventor(Icon.ARROW, Era.II);
        player.addInventor(card2);
        assertEquals(2, player.countNumCharacters());
    }

    @Test
    void countNumBuildings() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        Effect effect = new EffectDraw(2, IDEffect.D1);
        Building building = new Building(2, 3,effect, Era.II);
        building.era = Era.I;
        assertEquals(1, player.countNumBuildings());
    }

    @Test
    void getNumArtists() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        assertEquals(0, player.getNumArtists());
        Artist card = new Artist(Era.II);
        player.addArtist(card);
        assertEquals(1, player.getNumArtists());
    }

    @Test
    void getNumGatherers() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        assertEquals(0, player.getNumGatherers());
        Gatherer card = new Gatherer(Era.II);
        player.addGatherer(card);
        assertEquals(1, player.getNumGatherers());
    }

    @Test
    void getNumInventors() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        assertEquals(0, player.getNumInventors());
        Inventor card = new Inventor(Icon.ARROW, Era.II);
        player.addInventor(card);
        assertEquals(1, player.getNumInventors());
    }

    @Test
    void getNumHunters() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        assertEquals(0, player.getNumHunters());
        Hunter card = new Hunter(true, Era.II);
        player.addHunter(card);
        assertEquals(1, player.getNumHunters());
    }

    @Test
    void getNumShamans() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        assertEquals(0, player.getNumShamans());
        Shaman card = new Shaman(3, Era.I);
        player.addShaman(card);
        assertEquals(1, player.getNumShamans());
    }

    @Test
    void getNumBuilders() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        assertEquals(0, player.getNumBuilders());
        Builder card = new Builder(1, 2, Era.I);
        player.addBuilder(card);
        assertEquals(1, player.getNumBuilders());
    }

    @Test
    void getNumStars() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        assertEquals(0, player.getNumStars());
        Shaman card = new Shaman(3, Era.I);
        player.addShaman(card);
        assertEquals(3, player.getNumStars());
        Shaman card2 = new Shaman(4, Era.I);
        player.addShaman(card2);
        assertEquals(7, player.getNumStars());
    }

    @Test
    void countBuildersPp() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        assertEquals(0, player.countBuildersPp());
        Builder card = new Builder(1, 2, Era.I);
        player.addBuilder(card);
        assertEquals(2, player.countBuildersPp());
        Builder card2 = new Builder(1, 50, Era.I);
        player.addBuilder(card2);
        assertEquals(52, player.countBuildersPp());
    }

    @Test
    void countTribePp() {
        Game game = new Game(new ArrayList<>());
        Player player = new Player("Elisa");
        assertEquals(0, player.countBuildersPp());
        Builder card = new Builder(1, 2, Era.I);
        player.addBuilder(card);
        assertEquals(2, player.countBuildersPp());
        Artist artist = new Artist(Era.II);
        player.addArtist(artist);
        assertEquals(4, player.countBuildersPp());
        Inventor card2 = new Inventor(Icon.ARROW, Era.II);
        player.addInventor(card2);
        assertEquals(5, player.countBuildersPp());
        Effect effect = new EffectDraw(2, IDEffect.D1);
        Building building = new Building(2, 3,effect, Era.II);
        building.era = Era.I;
        assertEquals(7, player.countBuildersPp());
    }

    @Test
    void getGame() {
        //help non lo so
    }
}