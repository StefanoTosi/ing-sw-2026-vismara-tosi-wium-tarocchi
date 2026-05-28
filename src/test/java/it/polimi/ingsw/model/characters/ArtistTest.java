package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArtistTest {

    @Test
    void addToPlayer() throws IllegalActionException {
        // Instantiate a player and a card
        Player p = new Player("Gilles");
        Card a = new Artist(Era.I);

        // Check it gets added correctly
        assertEquals(0, p.getArtists().size());
        a.addToPlayer(p);
        assertEquals(1, p.getArtists().size());

        // Check it gets counted correctly
        assertEquals(a, p.getArtists().get(0));
    }

    @Test
    void toDTO(){
        Artist a = new Artist(Era.I);
        //needed to check manualy if the print is correct
        for(StringBuilder line : a.toDTO().printCard()){
            System.out.println(line);
        }
    }
}