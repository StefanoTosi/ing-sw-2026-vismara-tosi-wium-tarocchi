package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Building;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EffectEndTurnTest {

    @Test
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