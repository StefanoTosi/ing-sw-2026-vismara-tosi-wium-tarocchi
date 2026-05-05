package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.events.Event;
import it.polimi.ingsw.model.events.Sustenance;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;
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
        List<Event> events = bottomRow.stream().filter(card -> card instanceof Event).map(card -> (Event)card).toList();

        List<Event> orderedEvents = new ArrayList<>(events);

        // Move Sustenance events to solve them last
        orderedEvents.sort(
                Comparator.comparing(
                        event -> event instanceof Sustenance)
        );


        for (Event event : events) {
            event.applyEffect(game.getPlayers());
            System.out.println("Resolved event " + event.getName());
        }

        bottomRow.removeAll(orderedEvents);

        // Transition to EndTurnState
        System.out.println("Finished resolving events");
        EndTurnState e = new EndTurnState(game);
    }
}
