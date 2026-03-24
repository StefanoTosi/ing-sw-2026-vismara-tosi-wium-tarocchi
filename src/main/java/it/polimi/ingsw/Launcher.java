package it.polimi.ingsw;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Offer;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import javafx.application.Application;

import java.util.List;

public class Launcher {
    public static void main(String[] args) throws IllegalActionException {
        Board b = new Board(5);
        System.out.println(b.getDeckE1Building().size());
        System.out.println(b.getDeckE2Building().size());
        System.out.println(b.getDeckE3Building().size());
        System.out.println(b.getDeckTribe().size() +
                b.getDeckE1Building().size() +
                b.getDeckE2Building().size() +
                        b.getDeckE3Building().size()

                );
        // Application.launch(HelloApplication.class, args);
    }
}
