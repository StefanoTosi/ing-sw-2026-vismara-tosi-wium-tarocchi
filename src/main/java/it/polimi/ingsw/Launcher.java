package it.polimi.ingsw;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.board.Board;
import javafx.application.Application;

import java.util.List;

public class Launcher {
    public static void main(String[] args) {
        Board b = new Board(5);
        List<Card> c = b.loadCards(5);
        for (Card card : c) {
            System.out.println(card.getName() + " " + card.getType());
        }
        System.out.println(c.size() + " cards loaded");
        // Application.launch(HelloApplication.class, args);
    }
}
