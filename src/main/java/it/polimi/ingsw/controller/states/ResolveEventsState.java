package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.events.Event;
import it.polimi.ingsw.model.events.Sustenance;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ResolveEventsState extends GameState {
    private final Game game;
    public ResolveEventsState(Game game) {
        this.game = game;
    }

    public StateDTO getStateDTO() {
        return StateDTO.RESOLVEEVENT;
    }

    public void resolveEvents() throws IllegalActionException, RemoteException {
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
            System.out.println("Resolved event " + event.getName());
        }

        // Transition to EndTurnState
        System.out.println("Finished resolving events");
        EndTurnState e = new EndTurnState(game);
    }
}
