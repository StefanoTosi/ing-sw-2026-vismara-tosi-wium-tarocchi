package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.events.Event;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AutomatedGame {
    private Game game;

    public AutomatedGame(List<Player> players, Random rng) throws IllegalActionException, RemoteException {
        this.game = new Game(new ArrayList<>(), rng);
        this.game.setNumPlayers(players.size());
        for (int i = 0; i < players.size(); i++) {
            this.game.getState().registerPlayer(this.game, players.get(i));
            players.get(i).setGame(this.game);
        }
    }

    public void step(Action action, Player player) throws IllegalActionException {
        try {
            action.execute(player);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalActionException("");
        }

    }

    public Game getGame() {
        return game;
    }

    public void printPlayerStats() {
        System.out.println("Player stats:");
        for (Player p : game.getPlayers()) {
            System.out.println("\t" + p.getName() + ": food: " + p.getFood() + "; pp: " + p.getPp());
        }
        System.out.println();
    }

    public void printEventsToResolve() {
        System.out.println("Events to resolve:");
        for (Card c : game.getBoard().getBottomRowTribe()) {
            if (c instanceof Event) {
                System.out.println("\t" + c.getName());
            }
        }
        System.out.println();
    }

    public void printPlayersOrder() {
        System.out.println("Players order: ");
        for (Player p : game.getPlayers()) {
            System.out.println("\t" + p.getName() + ": " + p.getOrder());
        }
        System.out.println();
    }

    public Player getCurrentPlayer() {
        List<Player> drawOrder = new ArrayList<>(game.getPlayers()
                .stream()
                .sorted((p1, p2) -> p1.getOrder() - p2.getOrder())
                .toList());

        return drawOrder.getFirst();
    }
}
