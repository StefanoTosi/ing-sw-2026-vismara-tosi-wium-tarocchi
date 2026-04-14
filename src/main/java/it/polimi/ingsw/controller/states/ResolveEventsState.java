package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.events.Event;
import it.polimi.ingsw.model.events.Sustenance;

import java.util.ArrayList;
import java.util.List;

public class ResolveEventsState extends GameState {
    private final Game game;
    public ResolveEventsState(Game game) {
        this.game = game;
    }

    public void resolveEvents() {
        List<Card> bottomRow = game.getBoard().getBottomRowTribe();
        List<Event> events = new ArrayList<>();
        for (Card card : bottomRow) {
            if (card instanceof Event) {
                events.add((Event) card);
            }
        }

        // Move Sustenance events to solve them last
        for(Event event : events) {
            if (event instanceof Sustenance) {
                events.remove(event);
                events.add(event);
            }
        }

        for (Event event : events) {
            event.applyEffect(game.getPlayers());
        }

        // Transition to EndTurnState
        System.out.println("Finished resolving events");
        game.setState(new EndTurnState(game));
    }
}
