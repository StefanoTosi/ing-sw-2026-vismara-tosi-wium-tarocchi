package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventorTest {

    @Test
    void getInventionIcon() {
        Inventor i = new Inventor(Icon.HOOK, Era.I);

        assertEquals(Icon.HOOK, i.getInventionIcon());
    }

    @Test
    void addToPlayer() {
        // Instantiate a player and a card
        Player p = new Player("Gilles");
        Inventor i = new Inventor(Icon.HOOK, Era.I);

        // Check it gets added correctly
        assertEquals(0, p.getInventors().size());
        i.addToPlayer(p);
        assertEquals(1, p.getInventors().size());

        // Check it gets counted correctly
        assertEquals(i, p.getInventors().get(0));
    }

    @Test
    void toDTO(){
        Inventor i = new Inventor(Icon.HOOK, Era.I);
        //needed to check manualy if the print is correct
        for(StringBuilder line : i.toDTO().printCard()){
            System.out.println(line);
        }
    }
}