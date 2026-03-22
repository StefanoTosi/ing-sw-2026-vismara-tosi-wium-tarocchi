package it.polimi.ingsw.model;

import it.polimi.ingsw.model.effects.Effect;
import it.polimi.ingsw.model.effects.EffectDraw;
import it.polimi.ingsw.model.effects.IDEffect;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildingTest {

    @Test
    void getType() {
        Effect effect = new EffectDraw(3, IDEffect.D1);
        Building building = new Building(1,2,effect, Era.II);

        assertEquals("Building", building.getType());
    }

    @Test
    void getPp() {
        Effect effect = new EffectDraw(3, IDEffect.D1);
        Building building = new Building(1,2,effect, Era.II);

        assertEquals(2, building.getPp());
    }
}