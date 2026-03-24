package it.polimi.ingsw.model.effects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EffectTest {

    @Test
    void applyEffectEventCavePaintings() {
    }

    @Test
    void applyEffectEventHunt() {
    }

    @Test
    void applyEffectEventShamanicRitual() {
    }

    @Test
    void applyEffectEventSustenance() {
    }

    @Test
    void applyEffectDraw() {
    }

    @Test
    void applyEffectEndGame() {
    }

    @Test
    void applyEffectEndTurn() {
    }

    @Test
    void applyEffectTileBonus() {
    }

    @Test
    void whenDrawn() {
    }
}

/*
@Test
    void applyEffectDraw() {
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

        EffectDraw ed = new EffectDraw(2, IDEffect.D1);

        assertEquals(0, player.getFood());
        assertEquals(0, player.countSets());
        ed.applyEffectDraw(player);
        assertEquals(0, player.getFood());

        player.addCard(a);
        player.addCard(b);
        player.addCard(g);
        player.addCard(h);
        player.addCard(inv);
        player.addCard(s);

        ed.applyEffectDraw(player);
        assertEquals(2, player.getFood());

        player.addCard(s2);
        player.addCard(a2);
        player.addCard(b2);
        player.addCard(g2);
        player.addCard(h2);
        player.addCard(inv2);

        ed.applyEffectDraw(player);
        assertEquals(4, player.getFood());

        //D2

        EffectDraw ed2 = new EffectDraw(3, IDEffect.D2);
        Inventor inv3 = new Inventor(Icon.LEATHER, Era.I);
        Inventor inv4 = new Inventor(Icon.LEATHER, Era.I);
        Inventor inv5 = new Inventor(Icon.SOUP, Era.I);
        player.addCard(inv3);
        player.addCard(inv5);

        ed2.applyEffectDraw(player);
        assertEquals(4, player.getFood());

        player.addCard(inv4);
        assertEquals(7, player.getFood());
    }
 */

/*
void applyEffectEndGame() {
        //aggiunge pp a fine gioco
        //EG4
        Player player = new Player("Elisa");
        EffectEndGame eg = new EffectEndGame(25,IDEffect.EG4);
        eg.applyEffectEndGame(player);
        assertEquals(25, player.getPp());

        //EG2
        //aggiunge 6pp per set di personaggio
        //diversi numeri di set, deve ricevere il punteggio giusto
        //set: 0, 1,2
        Player player2 = new Player("Lisa");
        EffectEndGame eg2 = new EffectEndGame(25,IDEffect.EG4);

        eg2.applyEffectEndGame(player2);
        assertEquals(0, player2.getPp());

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

        eg2.applyEffectEndGame(player2);
        assertEquals(6, player.getPp());

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

        eg2.applyEffectEndGame(player2);
        assertEquals(12, player.getPp());

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

        eg2.applyEffectEndGame(player2);
        assertEquals(18, player.getPp());

        //guadagno doppio dei pp su costruttori

        Player player3 = new Player("Gilles");
        EffectEndGame eg3 = new EffectEndGame(0,IDEffect.EG1);

        eg3.applyEffectEndGame(player3);
        assertEquals(0, player3.getPp());

        Builder b3 = new Builder(2,4,Era.I);
        player3.addCard(b3);
        eg3.applyEffectEndGame(player3);

        assertEquals(8, player3.getPp());

        //in base a personaggio rappresentato dall'effetto da pp rappresentati a fine gioco
        //per ogni carta di quel tipo
    }
 */

/*
void applyEffectEndTurn() {
        //posso prendere solo building e character
        //se righe vuote non pesco niente
        //pesco solo da fila superiori
        Effect effect = new EffectDraw(3, IDEffect.D1);
        Building building = new Building(1,2,effect, Era.II);
        Player player = new Player("Elisa");
        Artist a = new Artist(Era.I);
        Builder b = new Builder(2,2,Era.I);
        Gatherer g = new Gatherer(Era.I);
        Hunter h = new Hunter(true, Era.I);
        Inventor inv = new Inventor(Icon.ARROW, Era.I);
        Shaman s = new Shaman(2, Era.I);
        Shaman s2 = new Shaman(2, Era.I);
        Shaman s3 = new Shaman(2, Era.I);

        EffectEndTurn e = new EffectEndTurn(IDEffect.ET2);
        e.applyEffectEndTurn(player, building);
        assertEquals(1, player.getBuildings().size());
        e.applyEffectEndTurn(player, a);
        assertEquals(1, player.getNumArtists());
        e.applyEffectEndTurn(player, b);
        assertEquals(1, player.getNumBuilders());
        e.applyEffectEndTurn(player, g);
        assertEquals(1, player.getNumGatherers());
        e.applyEffectEndTurn(player, s);
        assertEquals(1, player.getNumShamans());
        e.applyEffectEndTurn(player, h);
        assertEquals(1, player.getNumHunters());
        e.applyEffectEndTurn(player, inv);
        assertEquals(1, player.getNumInventors());
        e.applyEffectEndTurn(player, s2);
        assertEquals(2, player.getNumShamans());
        e.applyEffectEndTurn(player, s3);
        assertEquals(3, player.getNumShamans());
    }
}
 */

/*
void applyEffectEvent() {
        //sconto sul cibo durante evento sostentamento
        //ES1
        //ESC1
        //ESC2
        //ESC3

        //non perdi pp se meno icone di altri player
    }
 */

/*
@Test
    void applyEffectTileBonus() {
        //prendi e 1 cibo extra
        //ET1

        Player player = new Player("Elisa");
        EffectTileBonus e = new EffectTileBonus(IDEffect.ET1);
        e.applyEffectTileBonus(player);

        assertEquals(1, player.getFood());
    }
 */