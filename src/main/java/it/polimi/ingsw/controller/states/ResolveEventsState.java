package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.events.Event;
import it.polimi.ingsw.model.events.Sustenance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ResolveEventsState extends GameState {
    private final Game game;
    private Board board;
    public ResolveEventsState(Game game) {
        this.game = game;
        this.board = game.getBoard();
    }

    public void resolveEvents() {
        List<Card> bottomRow = game.getBoard().getBottomRowTribe();
        List<Event> events = new ArrayList<>();
        for (Card card : bottomRow) {
            if (card instanceof Event) {
                events.add((Event) card);
            }
        }

        //Move Sustenance events to solve them last
        for(Event event : events) {
            if (event instanceof Sustenance) {
                events.remove(event);
                events.add(event);
            }
        }

        //La risoluzione in ordine di era a parità di tipologia può essere data per scontata visto l'ordine delle carte nel mazzo?

        for(Event event : events) {
            event.applyEffect(game.getPlayers());
        }
    }
}
