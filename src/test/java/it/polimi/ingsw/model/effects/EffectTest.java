package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.controller.states.FillBoardState;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class EffectTest {

    /**
     * Tests the ECP effect
     */
    @Test
    void applyEffectEventCavePaintings() {
        Building b = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ECP);
        Player player = new Player("Elisa");
        Artist a1 = new Artist(Era.I);
        Artist a2 = new Artist(Era.I);
        Artist a3 = new Artist(Era.I);

        // Without any artists, the food bonus is 0
        b.getEffect().applyEffectEventCavePaintings(player, b);
        assertEquals(0, player.getFood());

        // With 3 artists, the food bonus is 3
        player.addArtist(a1);
        player.addArtist(a2);
        player.addArtist(a3);

        b.getEffect().applyEffectEventCavePaintings(player, b);
        assertEquals(3, player.getFood());
    }

    /**
     * Tests the EH effect
     */
    @Test
    void applyEffectEventHunt() {
        Building b = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.EH);
        Player player = new Player("Elisa");
        Hunter h1 = new Hunter(false,Era.I);
        Hunter h2 = new Hunter(false,Era.I);
        Hunter h3 = new Hunter(false,Era.I);

        // A player without hunters gets no food bonus
        b.getEffect().applyEffectEventHunt(player, b);
        assertEquals(0, player.getFood());
        assertEquals(0, player.getFood());

        // For each added hunter, the effect of the application of the event grows by 1
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

    /**
     * Tests the ESC1 effect
     */
    @Test
    void applyEffectEventShamanicRitual1() {
        Building b1 = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ESC1);
        Player p3 = new Player("Elisa");
        Player p4 = new Player("Lisa");

        Game game = new Game(new ArrayList<Player>(Arrays.asList(p3, p4)), null);

        p3.setGame(game);
        p4.setGame(game);

        Shaman s1 = new Shaman(3, Era.I);
        Shaman s2 = new Shaman(3, Era.I);
        Shaman s3 = new Shaman(3, Era.I);

        try {
            p3.addCard(s1);
            p3.addCard(s2);
            p4.addCard(s3);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        // A player without the fewest number of stars still lose pp
        b1.getEffect().applyEffectEventShamanicRitual(p3, b1);
        assertEquals(false, p3.getDontLosePp());

        // A player with the fewest number of stars doesn't lose pp
        b1.getEffect().applyEffectEventShamanicRitual(p4, b1);
        assertEquals(true, p4.getDontLosePp());
    }

    /**
     * Tests the ESC3 effect
     */
    @Test
    void applyEffectEventShamanicRitual3() {
        Building b = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ESC3);
        Player p1 = new Player("Elisa");
        Player p2 = new Player("Lisa");

        Game game = new Game(new ArrayList<Player>(Arrays.asList(p1, p2)), null);

        p1.setGame(game);
        p2.setGame(game);

        Shaman s1 = new Shaman(3, Era.I);
        Shaman s2 = new Shaman(3, Era.I);
        Shaman s3 = new Shaman(3, Era.I);

        // A player with no shamans gets no double pp
        b.getEffect().applyEffectEventShamanicRitual(p1, b);
        assertEquals(false, p1.getDoublePp());

        // A player with the most shamans gets double pp

        try {
            p1.addCard(s1);
            p1.addCard(s2);
            p2.addCard(s3);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        b.getEffect().applyEffectEventShamanicRitual(p1, b);
        assertEquals(true, p1.getDoublePp());

        // A player with equal number of shamans as other players gets no double pp
        try {
            p2.addCard(s1);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        b.getEffect().applyEffectEventShamanicRitual(p1, b);
        assertEquals(false, p1.getDoublePp());
    }

    /**
     * Tests the ES1 effect
     */
    @Test
    void applyEffectEventSustenance1() {
        Player p1 = new Player("Elisa");
        Building b1 = new Building(Era.I, 5, 5, 5, (p) -> p.getNumGatherers(), Effect.ES1);

        // A player with no gatherers gets no food discount
        b1.getEffect().applyEffectEventSustenance(p1, b1);
        assertEquals(0, p1.getFoodDiscount());

        Gatherer g1 = new Gatherer(Era.I);
        Gatherer g2 = new Gatherer(Era.I);
        Gatherer g3 = new Gatherer(Era.I);

        // A player with 3 gatherers gets 3 food discount
        g1.addToPlayer(p1);
        g2.addToPlayer(p1);
        g3.addToPlayer(p1);

        b1.getEffect().applyEffectEventSustenance(p1, b1);
        assertEquals(3, p1.getFoodDiscount());

        p1.setFoodDiscount(0);

        // A player with 3 gatherers and 2 buildings with ES1 gets 6 food discount
        Building b2 = new Building(Era.I, 5, 5, 5, (p) -> p.getNumGatherers(), Effect.ES1);
        b1.getEffect().applyEffectEventSustenance(p1, b1);
        b2.getEffect().applyEffectEventSustenance(p1, b1);
        assertEquals(6, p1.getFoodDiscount());
    }

    /**
     * Tests the D1 effect
     */
    @Test
    void applyEffectDraw1() {
        Player player = new Player("Elisa");
        Artist a1 = new Artist(Era.I);
        Builder b1 = new Builder(2,2,Era.I);
        Gatherer g1 = new Gatherer(Era.I);
        Hunter h1 = new Hunter(true, Era.I);
        Inventor inv1 = new Inventor(Icon.ARROW, Era.I);
        Shaman s1 = new Shaman(2, Era.I);

        Artist a2 = new Artist(Era.I);
        Builder b2 = new Builder(2,2,Era.I);
        Gatherer g2 = new Gatherer(Era.I);
        Hunter h2 = new Hunter(true, Era.I);
        Inventor inv2 = new Inventor(Icon.ARROW, Era.I);
        Shaman s2 = new Shaman(2, Era.I);

        Building building = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.D1);

        // A player without any sets does not get any food
        assertEquals(0, player.getFood());
        assertEquals(0, player.countSets());
        building.getEffect().applyEffectDraw(player, 0, s2);
        assertEquals(0, player.getFood());

        // A player with a set gets food
        try {
            player.addCard(a1);
            player.addCard(b1);
            player.addCard(g1);
            player.addCard(h1);
            player.addCard(inv1);
            player.addCard(s1);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }


        building.getEffect().applyEffectDraw(player, 0, s2);
        assertEquals(5, player.getFood());

        // A player with a set gets food
        try {
            player.addCard(s2);
            player.addCard(a2);
            player.addCard(b2);
            player.addCard(g2);
            player.addCard(h2);
            player.addCard(inv2);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }


        building.getEffect().applyEffectDraw(player, 0, s2);
        assertEquals(10, player.getFood());
    }

    /**
     * Tests the D2 effect
     */
    @Test
    void applyEffectDraw2() {
        Player player = new Player("Elisa");
        Building building = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.D2);
        Inventor inv1 = new Inventor(Icon.HOOK, Era.I);
        Inventor inv2 = new Inventor(Icon.SOUP, Era.I);
        Inventor inv3 = new Inventor(Icon.LEATHER, Era.I);
        Inventor inv4 = new Inventor(Icon.LEATHER, Era.I);
        Inventor inv5 = new Inventor(Icon.SOUP, Era.I);
        try {
            player.addCard(inv3);
            player.addCard(inv5);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }


        // A player with a LEATHER that draws a LEATHER completes a pair and thus gains food
        building.getEffect().applyEffectDraw(player, 0, inv4);
        assertEquals(3, player.getFood());
        try {
            player.addCard(inv4);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        // A player without a HOOK that draws a HOOK does not complete a pair and thus does not gain food
        building.getEffect().applyEffectDraw(player, 0, inv1);
        assertEquals(3, player.getFood());
        try {
            player.addCard(inv1);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        // A player with a SOUP that draws a SOUP completes a pair and thus gains food
        building.getEffect().applyEffectDraw(player, 0, inv2);
        assertEquals(6, player.getFood());
    }

    /**
     * Tests the EG1 effect
     */
    @Test
    void applyEffectEndGame1() {
        Player player3 = new Player("Gilles");
        Building building = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.EG1);
        building.addToPlayer(player3);

        // A player without any builders should not see any effect
        building.getEffect().applyEffectEndGame(player3, building);
        assertEquals(0, player3.getPp());

        // A player with builders should see double the number of pp the builder gives
        // We test only half of it, since the other half is granted by the EndGameState
        Builder b3 = new Builder(2,4,Era.I);
        try {
            player3.addCard(b3);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        building.getEffect().applyEffectEndGame(player3, building);
        assertEquals(4, player3.getPp());

    }

    /**
     * Tests the EG2 effect
     */
    @Test
    void applyEffectEndGame2() {
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

        try {
            player.addCard(a);
            player.addCard(b);
            player.addCard(g);
            player.addCard(inv);
            player.addCard(s);
            player.addCard(h);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        building.getEffect().applyEffectEndGame(player, building);
        assertEquals(6, player.getPp());

        // With 2 set, the player should gain 12 pp
        Artist a1 = new Artist(Era.I);
        Builder b1 = new Builder(2,2,Era.I);
        Gatherer g1 = new Gatherer(Era.I);
        Hunter h1 = new Hunter(true, Era.I);
        Inventor inv1 = new Inventor(Icon.ARROW, Era.I);
        Shaman s1 = new Shaman(2, Era.I);

        try {
            player.addCard(a1);
            player.addCard(b1);
            player.addCard(g1);
            player.addCard(inv1);
            player.addCard(s1);
            player.addCard(h1);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        building.getEffect().applyEffectEndGame(player, building);
        assertEquals( 6 + 12, player.getPp());

        // With 3 set, the player should gain 18 pp
        Artist a2 = new Artist(Era.I);
        Builder b2 = new Builder(2,2,Era.I);
        Gatherer g2 = new Gatherer(Era.I);
        Hunter h2 = new Hunter(true, Era.I);
        Inventor inv2 = new Inventor(Icon.ARROW, Era.I);
        Shaman s2 = new Shaman(2, Era.I);

        try {
            player.addCard(a2);
            player.addCard(b2);
            player.addCard(g2);
            player.addCard(inv2);
            player.addCard(s2);
            player.addCard(h2);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        building.getEffect().applyEffectEndGame(player, building);
        assertEquals(6 + 12 + 18, player.getPp());
    }

    /**
     * Tests the EG3 effect
     */
    @Test
    void applyEffectEndGame3() {
        Player player = new Player("Elisa");
        Building building = new Building(Era.I, 5, 5, 5, (p) -> p.getNumHunters(), Effect.EG3);
        building.addToPlayer(player);

        // Without any hunters, the player receives no bonus
        building.getEffect().applyEffectEndGame(player, building);
        assertEquals(0, player.getPp());

        // With 2 hunters, the player receives 10 pp
        Hunter h1 = new Hunter(true, Era.I);
        Hunter h2 = new Hunter(true, Era.I);
        h1.addToPlayer(player);
        h2.addToPlayer(player);

        building.getEffect().applyEffectEndGame(player, building);
        assertEquals(10, player.getPp());
    }

    /**
     * Tests the EG4 effect
     */
    @Test
    void applyEffectEndGame4() {
        Player player = new Player("Elisa");
        Building building = new Building(Era.I, 5, 5, 25, (p) -> null, Effect.EG4);

        // A player with this building gains 25 pp
        building.getEffect().applyEffectEndGame(player, building);
        assertEquals(25, player.getPp());
    }

    /**
     * Tests the ET1 effect
     */
    @Test
    void applyEffectTileBonus() {
        Player p1 = new Player("Elisa");
        Player p2 = new Player("Gilles");
        Building building = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ET1);
        Game game = new Game(Arrays.asList(p1, p2), new Random(42));
        game.getBoard().initialize(2);

        p1.setGame(game);
        p2.setGame(game);

        p1.setOrder(1);
        p2.setOrder(0);

        // The player on the bottom order tile gets no additional food bonus
        building.getEffect().applyEffectTileBonus(p1, building);
        assertEquals(0, p1.getFood());

        // The player on the bottom order tile gets 1 additional food bonus
        building.getEffect().applyEffectTileBonus(p2, building);
        assertEquals(1, p2.getFood());
    }

    /**
     * Tests the ET2 effect
     */
    @Test
    void applyEffectEndTurn() {
        Building building = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ET2);
        Player player = new Player("Elisa");

        assertEquals(false, player.getCanPickFromTop());
        building.getEffect().applyEffectEndTurn(player);
        assertEquals(true, player.getCanPickFromTop());
    }

    /**
     * Tests the ESC2 effect
     */
    @Test
    void whenDrawn() {
        Player p1 = new Player("Elisa");
        Player p2 = new Player("Lisa");
        Building b1 = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ESC2);
        b1.getEffect().whenDrawn(p1);

        Game game = new Game(new ArrayList<Player>(Arrays.asList(p1, p2)), null);

        Shaman s1 = new Shaman(3, Era.I);
        Shaman s2 = new Shaman(3, Era.I);
        Shaman s3 = new Shaman(3, Era.I);

        try {
            p1.addCard(s1);
            p1.addCard(s2);
            p2.addCard(s3);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        assertEquals(3, p1.getAdditionalStars());

        Building b3 = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ESC2);
        Building b4 = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ESC2);
        b3.getEffect().whenDrawn(p1);
        b4.getEffect().whenDrawn(p1);

        assertEquals(9, p1.getAdditionalStars());
    }
}
