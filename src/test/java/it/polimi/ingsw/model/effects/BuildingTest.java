package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.Builder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildingTest {

    @Test
    void addToPlayer() {
        Player player = new Player("Elisa");
        Building b = new Building(Era.I, 5, 5, 5, null, null, Effect.ECP, 0);
        b.addToPlayer(player);
        assertEquals(1, player.getBuildings().size());
    }

    @Test
    void getCost() {
        Building b = new Building(Era.I, 5, 5, 5, null, null, Effect.ECP, 0);
        assertEquals(5, b.getCost());
    }

    @Test
    void getPp() {
        Building b = new Building(Era.I, 5, 5, 5, null, null, Effect.ECP, 0);
        assertEquals(5, b.getPp());
    }

    @Test
    void getEffectPp() {
        Building b = new Building(Era.I, 5, 5, 5, null, null, Effect.ECP, 0);
        assertEquals(5, b.getEffectPp());
    }

    @Test
    void getEffect() {
        Building b = new Building(Era.I, 5, 5, 5, null, null, Effect.ECP, 0);
        assertEquals(Effect.ECP, b.getEffect());
    }

    @Test
    void getEffectCharacter() {
        Building b = new Building(Era.I, 4, 4, 0, null, "Gatherer", Effect.ES1, 0);
        assertEquals(b.getEffectCharacter(), b.toDTO().getEffectCharacter());
    }

    @Test
    void discountedCost() {
        Building b1 = new Building(Era.I, 7, 5, 5, null, null, Effect.ESC2, 0);
        Player player = new Player("Elisa");

        Builder builder1 = new Builder(2, 2, Era.I, 0);
        player.addBuilder(builder1);
        assertEquals(5, b1.discountedCost(player));

        //Builder discount is higher than building cost
        Builder builder2 = new Builder(8, 2, Era.I, 0);
        player.addBuilder(builder2);
        assertEquals(0, b1.discountedCost(player));
    }

    @Test
    void toDTO() {
        Building b = new Building(Era.I, 4, 4, 0, null, "Gatherer", Effect.ES1, 0);
        //check manually
        for(StringBuilder line : b.toDTO().printCard()){
            System.out.println(line);
        }
    }

}