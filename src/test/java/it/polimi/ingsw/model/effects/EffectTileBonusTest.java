package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EffectTileBonusTest {

    @Test
    void applyEffectTileBonus() {
        //prendi e 1 cibo extra
        //ET1

        Player player = new Player("Elisa");
        EffectTileBonus e = new EffectTileBonus(IDEffect.ET1);
        e.applyEffectTileBonus(player);

        assertEquals(1, player.getFood());
    }
}