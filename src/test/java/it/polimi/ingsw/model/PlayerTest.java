package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.effects.Effect;
import it.polimi.ingsw.model.events.ShamanicRitual;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void Player() {
        String name = "A";
        List<Artist> artists = List.of(new Artist(Era.I, 0));
        List<Gatherer> gatherers = List.of(new Gatherer(Era.I, 0));
        List<Hunter> hunters = List.of(new Hunter(false, Era.I, 0));
        List<Inventor> inventors = List.of(new Inventor(Icon.ARROW, Era.I, 0));
        List<Shaman> shamans = List.of(new Shaman(3, Era.I, 0));
        List<Builder> builders = List.of(new Builder(3, 3, Era.I, 0));
        List<Building> buildings = List.of(new Building(Era.I, 0, 0, 0, null, null, null, 0));
        int pp = 10;
        int food = 9;
        int order = 'f';
        char offer = 'g';
        Totem totem = Totem.ORANGE;

        Player p = new Player(name, artists, gatherers, hunters, inventors, shamans, builders, buildings, pp, food, order, offer, totem);

        assertEquals(name, p.getName());
        assertEquals(artists, p.getArtists());
        assertEquals(gatherers, p.getGatherers());
        assertEquals(hunters, p.getHunters());
        assertEquals(inventors, p.getInventors());
        assertEquals(shamans, p.getShamans());
        assertEquals(builders, p.getBuilders());
        assertEquals(buildings, p.getBuildings());
        assertEquals(pp, p.getPp());
        assertEquals(food, p.getFood());
        assertEquals(order, p.getOrder());
        assertEquals(offer, p.getOffer());
        assertEquals(totem, p.getTotem());
        assertEquals(0, p.getFoodDiscount());
        assertEquals(0, p.getAdditionalStars());
        assertFalse(p.getDontLosePp());
        assertFalse(p.getDoublePp());
        assertFalse(p.getCanPickFromTop());
        assertNull(p.getGame());
    }

    @Test
    void getName() {
        Player player1 = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player1, player2, player3)), null);

        assertEquals("Elisa", player1.getName());
    }

    @Test
    void getPp() {
        Player player1 = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player1, player2, player3)), null);
        player1.setPp(42);
        assertEquals(42, player1.getPp());
    }

    @Test
    void setPp() {
        Player player1 = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player1, player2, player3)), null);
        player1.setPp(42);
        assertEquals(42, player1.getPp());
    }

    @Test
    void addPp() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        player.addPp(42);
        assertEquals(42, player.getPp());
        player.addPp(-3);
        assertEquals(39, player.getPp());
        player.addPp(-40);
        assertEquals(-1, player.getPp());
    }

    @Test
    void getFood() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        player.setFood(42);
        assertEquals(42, player.getFood());
    }

    @Test
    void setFood() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        player.setFood(42);
        assertEquals(42, player.getFood());
        assertThrows(IllegalArgumentException.class, ()->player.setFood(-4));
    }


    @Test
    void addFood() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        player.addFood(42);
        assertEquals(42, player.getFood());
        assertThrows(IllegalArgumentException.class, ()->player.addFood(-43));
    }


    @Test
    void addCard() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        //Card card = null;
        //player.addCard(card); me lo dà non valido come è giusto che sia
        assertEquals(0,player.countNumBuildings());
        assertEquals(0, player.countNumCharacters());
        Effect effect = Effect.EG2;
        Card card = new Building(Era.II,2,2, 2, null, null, effect, 0);
        try {
            player.addCard(card);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }
        assertEquals(1,player.countNumBuildings());
        Card card3 = new Building(Era.II,2,2, 2, null, null, effect, 0);
        try {
            player.addCard(card3);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }
        assertEquals(2,player.countNumBuildings());
        Card card1 = new Artist(Era.II, 0);
        try {
            player.addCard(card1);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }
        Card card2 = new ShamanicRitual(2, 2, Era.II, 0);
        assertThrows(IllegalActionException.class, ()->player.addCard(card2));
        assertEquals(1, player.countNumCharacters());

        Card card4 = new Artist(Era.II, 0);
        try {
            player.addCard(card4);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }
        assertEquals(2, player.countNumCharacters());
    }

    // verifica che aggiunge
    @Test
    void addArtist() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        Artist card = new Artist(Era.II, 0);
        player.addArtist(card);
        assertEquals(1, player.getNumArtists());
    }

    @Test
    void addGatherer() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        Gatherer card = new Gatherer(Era.II, 0);
        player.addGatherer(card);
        assertEquals(1, player.getNumGatherers());
    }

    @Test
    void addHunter() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        Hunter card = new Hunter(true, Era.I, 0);
        player.addHunter(card);
        assertEquals(1, player.getNumHunters());
    }

    @Test
    void addInventor() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        Inventor card = new Inventor(Icon.ARROW, Era.I, 0);
        player.addInventor(card);
        assertEquals(1, player.getNumInventors());
    }

    @Test
    void addShaman() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        Shaman card = new Shaman(2, Era.II, 0);
        player.addShaman(card);
        assertEquals(1, player.getNumShamans());
    }

    @Test
    void addBuilder() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        Builder card = new Builder(1, 2, Era.I, 0);
        player.addBuilder(card);
        assertEquals(1, player.getNumBuilders());
    }

    @Test
    void countSets() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        Artist artist = new Artist(Era.II, 0);
        player.addArtist(artist);
        Inventor inventor = new Inventor(Icon.ARROW, Era.I, 0);
        player.addInventor(inventor);
        assertEquals(0, player.countSets());
        Hunter hunter = new Hunter(true, Era.I, 0);
        player.addHunter(hunter);
        Shaman shaman = new Shaman(2, Era.II, 0);
        player.addShaman(shaman);
        Gatherer gatherer = new Gatherer(Era.II, 0);
        player.addGatherer(gatherer);
        Builder builder = new Builder(1, 2, Era.I, 0);
        player.addBuilder(builder);
        assertEquals(1, player.countSets());
        Artist artist2 = new Artist(Era.II, 0);
        player.addArtist(artist2);
        Inventor inventor2 = new Inventor(Icon.ARROW, Era.I, 0);
        player.addInventor(inventor2);
        Hunter hunter2 = new Hunter(true, Era.I, 0);
        player.addHunter(hunter2);
        Shaman shaman2 = new Shaman(2, Era.II, 0);
        player.addShaman(shaman2);
        Gatherer gatherer2 = new Gatherer(Era.II, 0);
        player.addGatherer(gatherer2);
        assertEquals(1, player.countSets());
        Builder builder2 = new Builder(1, 2, Era.I, 0);
        player.addBuilder(builder2);
        assertEquals(2, player.countSets());
    }


    @Test
    void addBuilding() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        assertEquals(0, player.countNumBuildings());
        Effect effect = Effect.EG2;
        Building building = new Building(Era.II,2,2, 2, null, null, effect, 0);
        building.era = Era.I;
        player.addBuilding(building);
        assertEquals(1, player.countNumBuildings());

    }

    @Test
    void countNumCharacters() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        assertEquals(0, player.countNumCharacters());
        Artist card = new Artist(Era.II, 0);
        player.addArtist(card);
        Inventor card2 = new Inventor(Icon.ARROW, Era.II, 0);
        player.addInventor(card2);
        assertEquals(2, player.countNumCharacters());
    }

    @Test
    void countNumBuildings() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        Effect effect = Effect.EG2;
        Building building = new Building(Era.II,2,2, 2, null, null, effect, 0);
        player.addBuilding(building);
        assertEquals(1, player.countNumBuildings());
    }

    @Test
    void getNumArtists() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        assertEquals(0, player.getNumArtists());
        Artist card = new Artist(Era.II, 0);
        player.addArtist(card);
        assertEquals(1, player.getNumArtists());
    }

    @Test
    void getNumGatherers() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        assertEquals(0, player.getNumGatherers());
        Gatherer card = new Gatherer(Era.II, 0);
        player.addGatherer(card);
        assertEquals(1, player.getNumGatherers());
    }

    @Test
    void getNumInventors() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        assertEquals(0, player.getNumInventors());
        Inventor card = new Inventor(Icon.ARROW, Era.II, 0);
        player.addInventor(card);
        assertEquals(1, player.getNumInventors());
    }

    @Test
    void getNumHunters() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        assertEquals(0, player.getNumHunters());
        Hunter card = new Hunter(true, Era.II, 0);
        player.addHunter(card);
        assertEquals(1, player.getNumHunters());
    }

    @Test
    void getNumShamans() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        assertEquals(0, player.getNumShamans());
        Shaman card = new Shaman(3, Era.I, 0);
        player.addShaman(card);
        assertEquals(1, player.getNumShamans());
    }

    @Test
    void getNumBuilders() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        assertEquals(0, player.getNumBuilders());
        Builder card = new Builder(1, 2, Era.I, 0);
        player.addBuilder(card);
        assertEquals(1, player.getNumBuilders());
    }

    @Test
    void getNumStars() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        assertEquals(0, player.getNumStars());
        Shaman card = new Shaman(3, Era.I, 0);
        player.addShaman(card);
        assertEquals(3, player.getNumStars());
        Shaman card2 = new Shaman(4, Era.I, 0);
        player.addShaman(card2);
        assertEquals(7, player.getNumStars());
    }

    @Test
    void countBuildersPp() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);
        assertEquals(0, player.countBuildersPp());
        Builder card = new Builder(1, 2, Era.I, 0);
        player.addBuilder(card);
        assertEquals(2, player.countBuildersPp());
        Builder card2 = new Builder(1, 50, Era.I , 0);
        player.addBuilder(card2);
        assertEquals(52, player.countBuildersPp());
    }

    @Test
    void countTribePp() {
        Player player = new Player("Elisa");
        Player player2 = new Player("Stefano");
        Player player3 = new Player("Gilles");
        Game game = new Game(new ArrayList<Player>(Arrays.asList(player, player2, player3)), null);

        assertEquals(0, player.countBuildersPp());

        Builder card = new Builder(1, 2, Era.I, 0);
        player.addBuilder(card);
        assertEquals(2, player.countTribePp());

        Artist artist1 = new Artist(Era.I, 0);
        player.addArtist(artist1);
        assertEquals(2, player.countTribePp());
        Artist artist2 = new Artist(Era.II, 0);
        player.addArtist(artist2);
        assertEquals(12, player.countTribePp());

        Inventor card2 = new Inventor(Icon.ARROW, Era.II, 0);
        player.addInventor(card2);
        assertEquals(13, player.countTribePp());

        Effect effect = Effect.EG2;
        Building building = new Building(Era.II,2,2, 2, null, null, effect, 0);
        building.era = Era.I;
        player.addBuilding(building);
        assertEquals(15, player.countTribePp());
    }


    @Test
    void addFoodDiscount() {
        Player player = new Player("Elisa");
        player.addFoodDiscount(3);
        assertEquals(3, player.getFoodDiscount());
    }
}