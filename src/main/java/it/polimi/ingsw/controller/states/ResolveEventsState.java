package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.events.Event;
import it.polimi.ingsw.model.events.EventResult;
import it.polimi.ingsw.model.events.Sustenance;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;
import java.util.*;

public class ResolveEventsState extends GameState {
    private final Game game;
    public ResolveEventsState(Game game) {
        this.game = game;
    }

    public StateDTO getStateDTO() {
        return StateDTO.RESOLVEEVENT;
    }

    public void resolveEvents() throws IllegalActionException, RemoteException {
        Map<String, List<EventResult>> eventResults = new LinkedHashMap<>();
        game.setEventResults(null);
        List<Card> bottomRow = game.getBoard().getBottomRowTribe();
        List<Event> events = new ArrayList<>(bottomRow.stream().filter(card -> card instanceof Event).map(card -> (Event) card).toList());

        //If it's the last turn, resolve the events in the top row too
        if(game.getTurnNumber() == 10) {
            events.addAll(game.getBoard().getTopRowTribe().stream().filter(card -> card instanceof Event).map(card -> (Event)card).toList());
        }

        // Move Sustenance events to solve them last
        events.sort(
                Comparator.comparing(
                        event -> event instanceof Sustenance)
        );

        for (Event event : events) {
            eventResults.put(event.getName(), event.applyEffect(game.getPlayers()));
            System.out.println("Resolved event " + event.getName());
        }

        game.setEventResults(eventResults);
        bottomRow.removeAll(events);

        // Transition to EndTurnState
        System.out.println("Finished resolving events");
        EndTurnState e = new EndTurnState(game);
    }
}
