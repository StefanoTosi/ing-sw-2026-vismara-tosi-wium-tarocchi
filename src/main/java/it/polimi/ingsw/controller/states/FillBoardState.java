package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.events.Event;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import javafx.util.Pair;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FillBoardState extends GameState {

    /**
     * Refills the top and bottom rows until they are full again. It also moves players back to the order tile and resolves the associated effects
     * @param game the game for which it refills the board
     * @throws IllegalActionException
     */
    public void refillBoard(Game game) throws IllegalActionException, RemoteException {
        Board board = game.getBoard();

        // Used to initially insert buildings from era I
        boolean boardUninitialized =    board.getTopRowTribe().size() == 0 &&
                                        board.getBottomRowTribe().size() == 0 &&
                                        board.getTopRowBuilding().size() == 0 &&
                                        board.getBottomRowBuilding().size() == 0;

        // Find current era
        Era currentEra;
        if (boardUninitialized) {
            currentEra = Era.I;
        } else {
            currentEra = board.getTopRowTribe().get(board.getTopRowTribe().size() - 1).getEra();
        }

        // Clear and move rows
        board.getBottomRowTribe().clear();
        board.setBottomRowTribe(new ArrayList<>(board.getTopRowTribe()));

        board.getTopRowTribe().clear();

        // Fill bottom row if board is uninitialized
        if (boardUninitialized) {
            while (board.getDeckTribe().size() > 0 && board.getBottomRowTribe().size() < game.getNumPlayers() + 1) {
                Card c = board.getDeckTribe().draw();

                if (c instanceof Event) {
                    board.getTopRowTribe().add(c);
                } else {
                    board.getBottomRowTribe().add(c);
                }
            }
        }

        // Fill top row
        while (board.getDeckTribe().size() > 0 && board.getTopRowTribe().size() < game.getNumPlayers() + 4) {
            Card c = board.getDeckTribe().draw();

            // Begin new era
            if (c.getEra() != currentEra) {
                currentEra = c.getEra();

                board.getBottomRowBuilding().clear();
                board.setBottomRowBuilding(new ArrayList<>(board.getTopRowBuilding()));

                board.getTopRowBuilding().clear();
                switch (currentEra) {
                    case Era.II:
                        while (board.getDeckE2Building().size() > 0) {
                            board.getTopRowBuilding().add((Building) board.getDeckE2Building().draw());
                        }
                        break;

                    case Era.III:
                        while (board.getDeckE3Building().size() > 0) {
                            board.getTopRowBuilding().add((Building) board.getDeckE3Building().draw());
                        }
                        break;

                    default:
                        throw new IllegalActionException("Invalid 'currentEra'");
                }
            }

            board.getTopRowTribe().add(c);
        }

        if (boardUninitialized) {
            // Place all buildings from era I
            while (board.getDeckE1Building().size() > 0) {
                board.getTopRowBuilding().add((Building) board.getDeckE1Building().draw());
            }
        }

        // Resolve end turn effects
        for (Player p : game.getPlayers()) {
            for (Building b : p.getBuildings()) {
                b.getEffect().applyEffectEndTurn(p, b);
            }
        }

        // Arrange players on the order tile
        List<Player> ordered;
        if (boardUninitialized) {
            // Randomly put layers on the order tile
            ordered = new ArrayList<>(game.getPlayers());
            Collections.shuffle(ordered);
        } else {
            // Move players back to the order tile
            ordered = game.getPlayers()
                    .stream()
                    .sorted((p1, p2) -> p1.getOffer() - p2.getOffer())
                    .toList();

            // Execute the ET1 effect
            game.getPlayers()
                    .stream()
                    .forEach(p -> p.getBuildings()
                            .stream()
                            .forEach(b -> b.getEffect().applyEffectTileBonus(p, b))
                    );
        }

        for (int i = 0; i < ordered.size(); i++) {
            ordered.get(i).setOrder(i);
            ordered.get(i).setOffer('\0');
        }

        // Once done filling, switch to ChooseOfferState
        game.notifyObserver();
        game.setState(new ChooseOfferState(game));
    }
}
