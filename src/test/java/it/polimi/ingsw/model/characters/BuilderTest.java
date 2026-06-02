package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.effects.Building;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuilderTest {

    @Test
    void getFoodDiscount() {
        Builder b = new Builder(4, 2, Era.I, 0);
        assertEquals(4, b.getFoodDiscount());
    }

    @Test
    void getPp() {
        Builder b = new Builder(4, 2, Era.I, 0);
        assertEquals(2, b.getPp());
    }

    @Test
    void addToPlayer() {
        // Instantiate a player and a card
        Player p = new Player("Gilles");
        Builder b = new Builder(4, 2, Era.I, 0);

        // Check it gets added correctly
        assertEquals(0, p.getBuilders().size());
        b.addToPlayer(p);
        assertEquals(1, p.getBuilders().size());

        // Check it gets counted correctly
        assertEquals(b, p.getBuilders().getFirst());
    }

    @Test
    void toDTO(){
        Builder b = new Builder(4, 2, Era.I, 0);
        //needed to check manually if the print is correct
        for(StringBuilder line : b.toDTO().printCard()){
            System.out.println(line);
        }
    }
}