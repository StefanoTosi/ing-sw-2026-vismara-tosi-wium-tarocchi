package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShamanTest {

    @Test
    void getStars() {
        Shaman s = new Shaman(42, Era.I, 0);

        assertEquals(42, s.getStars());
    }

    @Test
    void addToPlayer() {
        // Instantiate a player and a card
        Player p = new Player("Gilles");
        Shaman s = new Shaman(42, Era.I, 0);

        // Check it gets added correctly
        assertEquals(0, p.getShamans().size());
        s.addToPlayer(p);
        assertEquals(1, p.getShamans().size());

        // Check it gets counted correctly
        assertEquals(s, p.getShamans().get(0));
    }

    @Test
    void toDTO(){
        Shaman s = new Shaman(42, Era.I, 0);
        //needed to check manually if the print is correct
        for(StringBuilder line : s.toDTO().printCard()){
            System.out.println(line);
        }
    }
}