package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildingTest {

    @Test
    void addToPlayer() {
        Player player = new Player("Elisa");
        Building b = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ECP);
        b.addToPlayer(player);
        assertEquals(1, player.getBuildings().size());
    }

    @Test
    void getCost() {
        Building b = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ECP);
        assertEquals(5, b.getCost());
    }

    @Test
    void getPp() {
        Building b = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ECP);
        assertEquals(5, b.getPp());
    }

    @Test
    void getEffectPp() {
        Building b = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ECP);
        assertEquals(5, b.getEffectPp());
    }

    @Test
    void getEffect() {
        Building b = new Building(Era.I, 5, 5, 5, (p) -> null, Effect.ECP);
        assertEquals(Effect.ECP, b.getEffect());
    }
}