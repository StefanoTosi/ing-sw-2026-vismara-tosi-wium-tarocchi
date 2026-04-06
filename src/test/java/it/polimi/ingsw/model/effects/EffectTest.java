package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EffectTest {

    @Test
    void applyEffectEventCavePaintings() {
        //ECP
        Building b = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ECP);
        Player player = new Player("Elisa");
        Artist a1 = new Artist(Era.I);
        Artist a2 = new Artist(Era.I);
        Artist a3 = new Artist(Era.I);

        b.getEffect().applyEffectEventCavePaintings(player, b);
        assertEquals(0, player.getFood());

        player.addArtist(a1);
        player.addArtist(a2);
        player.addArtist(a3);

        b.getEffect().applyEffectEventCavePaintings(player, b);
        assertEquals(3, player.getFood());
    }

    @Test
    void applyEffectEventHunt() {
        //EH
        Building b = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.EH);
        Player player = new Player("Elisa");
        Hunter h1 = new Hunter(false,Era.I);
        Hunter h2 = new Hunter(false,Era.I);
        Hunter h3 = new Hunter(false,Era.I);

        b.getEffect().applyEffectEventHunt(player, b);
        assertEquals(0, player.getFood());
        assertEquals(0, player.getFood());

        player.addHunter(h1);
        b.getEffect().applyEffectEventHunt(player, b);
        assertEquals(1, player.getFood());
        assertEquals(1, player.getFood());

        player.addHunter(h2);
        b.getEffect().applyEffectEventHunt(player, b);
        assertEquals(3, player.getFood());
        assertEquals(3, player.getFood());

        player.addHunter(h3);
        b.getEffect().applyEffectEventHunt(player, b);
        assertEquals(6, player.getFood());
        assertEquals(6, player.getFood());
    }

    @Test
    void applyEffectEventShamanicRitual() {
        //ESC3
        Building b = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ESC3);
        Player p1 = new Player("Elisa");
        Player p2 = new Player("Lisa");

        Game game = new Game(new ArrayList<Player>(Arrays.asList(p1, p2)));

        Shaman s1 = new Shaman(3, Era.I);
        Shaman s2 = new Shaman(3, Era.I);
        Shaman s3 = new Shaman(3, Era.I);

        b.getEffect().applyEffectEventShamanicRitual(p1, b);
        assertEquals(false, p1.getDoublePp());

        p1.addCard(s1);
        p1.addCard(s2);
        p2.addCard(s3);

        b.getEffect().applyEffectEventShamanicRitual(p1, b);
        assertEquals(true, p1.getDoublePp());

        p2.addCard(s1);

        b.getEffect().applyEffectEventShamanicRitual(p1, b);
        assertEquals(false, p1.getDoublePp());

        //ESC1
        Building b1 = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ESC1);
        Player p3 = new Player("Elisa");
        Player p4 = new Player("Lisa");

        game = new Game(new ArrayList<Player>(Arrays.asList(p3, p4)));

        b1.getEffect().whenDrawn(p1);
        assertEquals(false, p1.getDontLosePp());

        p3.addCard(s1);
        p3.addCard(s2);
        p4.addCard(s3);

        b1.getEffect().applyEffectEventShamanicRitual(p3, b1);
        assertEquals(false, p3.getDontLosePp());
        b1.getEffect().applyEffectEventShamanicRitual(p4, b1);
        assertEquals(true, p4.getDontLosePp());

    }

    // TODO:
    @Test
    void applyEffectEventSustenance() {
        //ES1
        Player p1 = new Player("Elisa");
        Building b = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ES1);
        //Chiedi chiarimenti
    }

    @Test
    void applyEffectDrawD1() {
        //D1
        Player player = new Player("Elisa");
        Artist a = new Artist(Era.I);
        Builder b = new Builder(2,2,Era.I);
        Gatherer g = new Gatherer(Era.I);
        Hunter h = new Hunter(true, Era.I);
        Inventor inv = new Inventor(Icon.ARROW, Era.I);
        Shaman s = new Shaman(2, Era.I);

        Artist a2 = new Artist(Era.I);
        Builder b2 = new Builder(2,2,Era.I);
        Gatherer g2 = new Gatherer(Era.I);
        Hunter h2 = new Hunter(true, Era.I);
        Inventor inv2 = new Inventor(Icon.ARROW, Era.I);
        Shaman s2 = new Shaman(2, Era.I);

        Building building = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.D1);

        assertEquals(0, player.getFood());
        assertEquals(0, player.countSets());
        building.getEffect().applyEffectDraw(player, 0, s2);
        assertEquals(0, player.getFood());

        player.addCard(a);
        player.addCard(b);
        player.addCard(g);
        player.addCard(h);
        player.addCard(inv);
        player.addCard(s);

        building.getEffect().applyEffectDraw(player, 0, s2);
        assertEquals(5, player.getFood());

        player.addCard(s2);
        player.addCard(a2);
        player.addCard(b2);
        player.addCard(g2);
        player.addCard(h2);
        player.addCard(inv2);

        building.getEffect().applyEffectDraw(player, 0, s2);
        assertEquals(10, player.getFood());
    }

    @Test
    void applyEffectDrawD2() {
        //D2
        Player player = new Player("Elisa");
        Building building = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.D2);
        Inventor inv3 = new Inventor(Icon.LEATHER, Era.I);
        Inventor inv4 = new Inventor(Icon.LEATHER, Era.I);
        Inventor inv5 = new Inventor(Icon.SOUP, Era.I);
        player.addCard(inv3);
        player.addCard(inv5);

        building.getEffect().applyEffectDraw(player, 0, inv4);
        assertEquals(0, player.getFood());

        player.addCard(inv4);
        building.getEffect().applyEffectDraw(player, 1, inv4);
        assertEquals(3, player.getFood());
    }

    @Test
    void applyEffectEndGameEG1() {
        // Instantiate player and building
        Player player3 = new Player("Gilles");
        Building building = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.EG1);
        building.addToPlayer(player3);

        // A player without any builders should not see any effect
        building.getEffect().applyEffectEndGame(player3, building);
        assertEquals(0, player3.getPp());

        // A player with builders should see double the number of pp the builder gives
        // We test only half of it, since the other half is granted by the EndGameState
        Builder b3 = new Builder(2,4,Era.I);
        player3.addCard(b3);
        building.getEffect().applyEffectEndGame(player3, building);
        assertEquals(4, player3.getPp());

    }

    @Test
    void applyEffectEndGameEG2() {
        // Instantiate players and building
        Player player = new Player("Elisa");
        Building building = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.EG2);
        building.addToPlayer(player);

        // Without any cards, player2 shouldn't gain any pp
        building.getEffect().applyEffectEndGame(player, building);
        assertEquals(0, player.getPp());

        // With 1 set, the player should gain 6 pp
        Artist a = new Artist(Era.I);
        Builder b = new Builder(2,2,Era.I);
        Gatherer g = new Gatherer(Era.I);
        Hunter h = new Hunter(true, Era.I);
        Inventor inv = new Inventor(Icon.ARROW, Era.I);
        Shaman s = new Shaman(2, Era.I);

        player.addCard(a);
        player.addCard(b);
        player.addCard(g);
        player.addCard(inv);
        player.addCard(s);
        player.addCard(h);

        building.getEffect().applyEffectEndGame(player, building);
        assertEquals(6, player.getPp());

        // With 2 set, the player should gain 12 pp
        Artist a1 = new Artist(Era.I);
        Builder b1 = new Builder(2,2,Era.I);
        Gatherer g1 = new Gatherer(Era.I);
        Hunter h1 = new Hunter(true, Era.I);
        Inventor inv1 = new Inventor(Icon.ARROW, Era.I);
        Shaman s1 = new Shaman(2, Era.I);

        player.addCard(a1);
        player.addCard(b1);
        player.addCard(g1);
        player.addCard(inv1);
        player.addCard(s1);
        player.addCard(h1);

        building.getEffect().applyEffectEndGame(player, building);
        assertEquals( 6 + 12, player.getPp());

        // With 3 set, the player should gain 18 pp
        Artist a2 = new Artist(Era.I);
        Builder b2 = new Builder(2,2,Era.I);
        Gatherer g2 = new Gatherer(Era.I);
        Hunter h2 = new Hunter(true, Era.I);
        Inventor inv2 = new Inventor(Icon.ARROW, Era.I);
        Shaman s2 = new Shaman(2, Era.I);

        player.addCard(a2);
        player.addCard(b2);
        player.addCard(g2);
        player.addCard(inv2);
        player.addCard(s2);
        player.addCard(h2);

        building.getEffect().applyEffectEndGame(player, building);
        assertEquals(6 + 12 + 18, player.getPp());
    }

    // TODO:
    @Test
    void applyEffectEndGameEG3() {

    }

    @Test
    void applyEffectEndGameEG4() {
        Player player = new Player("Elisa");
        Building building = new Building(Era.I, 5, 5, 25, (p) -> null, Effect.EG4);
        building.getEffect().applyEffectEndGame(player, building);
        assertEquals(25, player.getPp());
    }

    @Test
    void applyEffectEndTurn() {
        //ET2
        Building building = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ET2);
        Player player = new Player("Elisa");
        Artist a = new Artist(Era.I);
        Builder b = new Builder(2,2,Era.I);
        Gatherer g = new Gatherer(Era.I);
        Hunter h = new Hunter(true, Era.I);
        Inventor inv = new Inventor(Icon.ARROW, Era.I);
        Shaman s = new Shaman(2, Era.I);
        Shaman s2 = new Shaman(2, Era.I);
        Shaman s3 = new Shaman(2, Era.I);
        Building b2 = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ET2);

        //building.getEffect().applyEffectEndGame(player, b2);
        //assertEquals(1, player.getBuildings().size());
        building.getEffect().applyEffectEndTurn(player,a);
        assertEquals(1, player.getNumArtists());
        building.getEffect().applyEffectEndTurn(player,b);
        assertEquals(1, player.getNumBuilders());
        building.getEffect().applyEffectEndTurn(player,g);
        assertEquals(1, player.getNumGatherers());
        building.getEffect().applyEffectEndTurn(player,s);
        assertEquals(1, player.getNumShamans());
        building.getEffect().applyEffectEndTurn(player,h);
        assertEquals(1, player.getNumHunters());
        building.getEffect().applyEffectEndTurn(player,inv);
        assertEquals(1, player.getNumInventors());
        building.getEffect().applyEffectEndTurn(player,s2);
        assertEquals(2, player.getNumShamans());
        building.getEffect().applyEffectEndTurn(player,s3);
        assertEquals(3, player.getNumShamans());
    }

    @Test
    void applyEffectTileBonus() {
        //ET1
        //asks for help
        Player player = new Player("Elisa");
        Building building = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ET1);
        building.getEffect().applyEffectTileBonus(player, building);

        assertEquals(1, player.getFood());
    }

    @Test
    void whenDrawn() {
        //ESC1
        Building b = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ESC1);
        Player p1 = new Player("Elisa");
        Player p2 = new Player("Lisa");

        Game game = new Game(new ArrayList<Player>(Arrays.asList(p1, p2)));

        Shaman s1 = new Shaman(3, Era.I);
        Shaman s2 = new Shaman(3, Era.I);
        Shaman s3 = new Shaman(3, Era.I);

        p1.addCard(s1);
        p1.addCard(s2);
        p2.addCard(s3);

        //ESC2
        Building b2 = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ESC2);
        b2.getEffect().whenDrawn(p1);

        assertEquals(3, p1.getAdditionalStars());

        Building b3 = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ESC2);
        Building b4 = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ESC2);
        b3.getEffect().whenDrawn(p1);
        b4.getEffect().whenDrawn(p1);

        assertEquals(9, p1.getAdditionalStars());
    }
}
