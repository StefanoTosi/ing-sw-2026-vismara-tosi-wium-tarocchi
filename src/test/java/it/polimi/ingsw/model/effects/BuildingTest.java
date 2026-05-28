package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildingTest {

    @Test
    void addToPlayer() {
        Player player = new Player("Elisa");
        Building b = new Building(Era.I, 5, 5, 5, null, null, Effect.ECP);
        b.addToPlayer(player);
        assertEquals(1, player.getBuildings().size());
    }

    @Test
    void getCost() {
        Building b = new Building(Era.I, 5, 5, 5, null, null, Effect.ECP);
        assertEquals(5, b.getCost());
    }

    @Test
    void getPp() {
        Building b = new Building(Era.I, 5, 5, 5, null, null, Effect.ECP);
        assertEquals(5, b.getPp());
    }

    @Test
    void getEffectPp() {
        Building b = new Building(Era.I, 5, 5, 5, null, null, Effect.ECP);
        assertEquals(5, b.getEffectPp());
    }

    @Test
    void getEffect() {
        Building b = new Building(Era.I, 5, 5, 5, null, null, Effect.ECP);
        assertEquals(Effect.ECP, b.getEffect());
    }

    @Test
    void getEffectCharacter() {
        Building b = new Building(Era.I, 4, 4, 0, null, "Gatherer", Effect.ES1);
        assertEquals(b.getEffectCharacter(), b.toDTO().getEffectCharacter());
    }

    @Test
    void toDTO() {
        Building b = new Building(Era.I, 4, 4, 0, null, "Gatherer", Effect.ES1);
        //check manually
        for(StringBuilder line : b.toDTO().printCard()){
            System.out.println(line);
        }
    }

}