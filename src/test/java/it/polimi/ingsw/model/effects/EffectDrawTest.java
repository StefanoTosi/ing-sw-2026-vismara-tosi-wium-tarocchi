package it.polimi.ingsw.model.effects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EffectDrawTest {

    @Test
    void applyEffectDraw() {
        //player con nuovo set riceve cibo
        //set già esistenti non valgono
        //set a cui manca una sola carta
        //set completo, ne finisco un altro
        //se 0 set resta 0

        //coppia di inventori con stessa icona, se nuova riceve cibo
        //2 inventor con stessa icona prima
        //inventor con stessa icona post
        //se 0 coppie, resta 0
    }

    @Test
    void getFood() {
        EffectDraw effect = new EffectDraw(3, IDEffect.D1);
        assertEquals(3, effect.getFood());
    }
}