package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.events.Event;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FillBoardState extends GameState {
    private final Game game;

    public FillBoardState(Game game) {
        this.game = game;
    }

    public StateDTO getStateDTO() {
        return StateDTO.FILLBOARD;
    }

    /**
     * Refills the top and bottom rows until they are full again. It also moves players back to the order tile and resolves the associated effects
     * @throws IllegalActionException
     */
    public void refillBoard() throws IllegalActionException, RemoteException {
        if (game.getTurnNumber() > 10) {
            game.setPlayerTurn(null);
            game.setState(new EndGameState(game));
            game.getState().calculateRankings();
            return;
        }
        Board board = game.getBoard();

        // Used to initially insert buildings from era I
        boolean boardUninitialized = board.getTopRowTribe().size() == 0 &&
                board.getBottomRowTribe().size() == 0 &&
                board.getTopRowBuilding().size() == 0 &&
                board.getBottomRowBuilding().size() == 0;

        // At the beginning of the game, randomly put players on the order tile
        List<Player> ordered;
        if (boardUninitialized) {
            ordered = new ArrayList<>(game.getPlayers());
            Collections.shuffle(ordered, game.getRng());

            for (int i = 0; i < ordered.size(); i++) {
                ordered.get(i).setOrder(i);
                ordered.get(i).setOffer('\0');
            }
        }

        // Find current era
        Era currentEra;
        if (boardUninitialized) {
            currentEra = Era.I;
        } else {
            if (board.getTopRowTribe().size() == 0) {
                currentEra = Era.III;
            } else {
                currentEra = board.getTopRowTribe().get(board.getTopRowTribe().size() - 1).getEra();
            }
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

        // Once done filling, switch to ChooseOfferState
        System.out.println("Refilled board");
        game.setState(new ChooseOfferState(game));
    }
}
