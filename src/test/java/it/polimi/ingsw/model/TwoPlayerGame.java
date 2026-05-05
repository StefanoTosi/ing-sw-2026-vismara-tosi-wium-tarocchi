package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.controller.actions.ChooseOfferAction;
import it.polimi.ingsw.controller.actions.DrawCardFromBottomAction;
import it.polimi.ingsw.controller.actions.DrawCardFromTopAction;
import it.polimi.ingsw.model.events.Event;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TwoPlayerGame {
    @Test
    void test() throws IllegalActionException, RemoteException {
        // Fix the rng
        Random rng = new Random(42);

        // Create automated game
        List<Player> players = new ArrayList<>(Arrays.asList(
                new Player("Gilles"),
                new Player("Elisa")
        ));

        AutomatedGame ag = new AutomatedGame(players, rng);
        Game game = ag.getGame();
        System.out.println("---------------------------------------");
        ag.printPlayerStats();
        ag.printEventsToResolve();
        ag.printPlayersOrder();

        // Turn 1
        System.out.println("Turn: " + game.getTurnNumber());

        // Choose offer tile
        ag.step(new ChooseOfferAction('C'), players.get(1));
        ag.step(new ChooseOfferAction('B'), players.get(0));

        // Draw cards
        System.out.println("Top row 0: ");
        System.out.println(game.getBoard().getTopRowTribe().get(0));

        System.out.println("Bottom row 0: ");
        System.out.println(game.getBoard().getBottomRowTribe().get(0));
        System.out.println();

        ag.step(new DrawCardFromBottomAction(0), players.get(0));
        ag.step(new DrawCardFromTopAction(0), players.get(1));

        assertEquals(1, players.get(0).getHunters().size());
        assertEquals(1, players.get(1).getInventors().size());

        // Resolve events
        System.out.println("---------------------------------------");
        ag.printPlayerStats();
        ag.printEventsToResolve();
        ag.printPlayersOrder();
        assertEquals(1, players.get(0).getFood());
        assertEquals(0, players.get(0).getPp());
        assertEquals(0, players.get(1).getFood());
        assertEquals(0, players.get(1).getPp());

        // Turn 2
        System.out.println("Turn: " + game.getTurnNumber());

        // Choose offer tile
        ag.step(new ChooseOfferAction('B'), players.get(0));
        ag.step(new ChooseOfferAction('C'), players.get(1));

        // Draw cards
        ag.step(new DrawCardFromBottomAction(0), players.get(0));
        ag.step(new DrawCardFromTopAction(0), players.get(1));

        System.out.println("---------------------------------------");
        ag.printPlayerStats();
        ag.printEventsToResolve();
        ag.printPlayersOrder();

        // Turn 3
        System.out.println("Turn: " + game.getTurnNumber());

        // Choose offer tile
        ag.step(new ChooseOfferAction('B'), players.get(0));
        ag.step(new ChooseOfferAction('C'), players.get(1));

        // Draw cards
        ag.step(new DrawCardFromBottomAction(1), players.get(0));
        ag.step(new DrawCardFromTopAction(1), players.get(1));

        System.out.println("---------------------------------------");
        ag.printPlayerStats();
        ag.printEventsToResolve();
        ag.printPlayersOrder();

        // Turn 4
        System.out.println("Turn: " + game.getTurnNumber());

        // Choose offer tile
        System.out.println(game.getState());
        ag.step(new ChooseOfferAction('B'), players.get(0));
        ag.step(new ChooseOfferAction('C'), players.get(1));

        // Draw cards
        ag.step(new DrawCardFromBottomAction(0), players.get(0));
        ag.step(new DrawCardFromTopAction(0), players.get(1));

        System.out.println("---------------------------------------");
        ag.printPlayerStats();
        ag.printEventsToResolve();
        ag.printPlayersOrder();

        // Turn 5
        System.out.println("Turn: " + game.getTurnNumber());

        // Choose offer tile
        ag.step(new ChooseOfferAction('B'), players.get(0));
        ag.step(new ChooseOfferAction('C'), players.get(1));

        // Draw cards
        ag.step(new DrawCardFromBottomAction(0), players.get(0));
        ag.step(new DrawCardFromTopAction(1), players.get(1));

        System.out.println("---------------------------------------");
        ag.printPlayerStats();
        ag.printEventsToResolve();
        ag.printPlayersOrder();

        // Turn 6
        System.out.println("Turn: " + game.getTurnNumber());

        // Choose offer tile
        ag.step(new ChooseOfferAction('B'), players.get(0));
        ag.step(new ChooseOfferAction('C'), players.get(1));

        // Draw cards
        ag.step(new DrawCardFromBottomAction(0), players.get(0));
        ag.step(new DrawCardFromTopAction(1), players.get(1));

        System.out.println("---------------------------------------");
        ag.printPlayerStats();
        ag.printEventsToResolve();
        ag.printPlayersOrder();

        // Turn 7
        System.out.println("Turn: " + game.getTurnNumber());

        // Choose offer tile
        ag.step(new ChooseOfferAction('B'), players.get(0));
        ag.step(new ChooseOfferAction('C'), players.get(1));

        // Draw cards
        ag.step(new DrawCardFromBottomAction(1), players.get(0));
        ag.step(new DrawCardFromTopAction(0), players.get(1));

        System.out.println("---------------------------------------");
        ag.printPlayerStats();
        ag.printEventsToResolve();
        ag.printPlayersOrder();

        // Turn 8
        System.out.println("Turn: " + game.getTurnNumber());

        // Choose offer tile
        ag.step(new ChooseOfferAction('B'), players.get(0));
        ag.step(new ChooseOfferAction('C'), players.get(1));

        // Draw cards
        ag.step(new DrawCardFromBottomAction(1), players.get(0));
        ag.step(new DrawCardFromTopAction(1), players.get(1));

        System.out.println("---------------------------------------");
        ag.printPlayerStats();
        ag.printEventsToResolve();
        ag.printPlayersOrder();

        // Turn 9
        System.out.println("Turn: " + game.getTurnNumber());

        // Choose offer tile
        ag.step(new ChooseOfferAction('B'), players.get(0));
        ag.step(new ChooseOfferAction('C'), players.get(1));

        // Draw cards
        ag.step(new DrawCardFromBottomAction(1), players.get(0));
        ag.step(new DrawCardFromTopAction(0), players.get(1));

        System.out.println("---------------------------------------");
        ag.printPlayerStats();
        ag.printEventsToResolve();
        ag.printPlayersOrder();

        // Turn 10
        System.out.println("Turn: " + game.getTurnNumber());

        // Choose offer tile
        ag.step(new ChooseOfferAction('B'), players.get(0));
        ag.step(new ChooseOfferAction('C'), players.get(1));

        // Draw cards
        ag.step(new DrawCardFromBottomAction(1), players.get(0));
        ag.step(new DrawCardFromTopAction(0), players.get(1));

        // Print ranking
        System.out.println("\nGame ended\nRanking: ");
        for (Player p : game.getRankings()) {
            System.out.println("\t" + p.getName());
        }
    }
}
