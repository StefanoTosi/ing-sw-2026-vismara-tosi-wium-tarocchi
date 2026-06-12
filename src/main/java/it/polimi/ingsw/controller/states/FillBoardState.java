package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.controller.SaveGames;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.events.Event;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Game state responsible for refilling the board at the start of a new round.
 * <p>
 * This state handles:
 * <ul>
 *     <li>Board initialization (first round setup)</li>
 *     <li>Transition between eras</li>
 *     <li>Refilling tribe and building rows</li>
 *     <li>Resetting player positions on the order tile</li>
 *     <li>Transition to {@link ChooseOfferState}</li>
 * </ul>
 */
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
    public void refillBoard() throws IllegalActionException, IOException {
        if (game.getTurnNumber() > 10) {
            game.setPlayerTurn(null);
            game.setState(new EndGameState(game));
            game.getState().calculateRankings();
            return;
        }
        Board board = game.getBoard();

        // Used to initially insert buildings from era I
        boolean boardUninitialized = board.getTopRowTribe().isEmpty() &&
                board.getBottomRowTribe().isEmpty() &&
                board.getTopRowBuilding().isEmpty() &&
                board.getBottomRowBuilding().isEmpty();

        // At the beginning of the game, randomly put players on the order tile
        // The 1st player receives 2 food tokens, the 2nd & 3rd receive 3 and the 4th & 5th receive 5.
        List<Player> ordered;
        if (boardUninitialized) {
            ordered = new ArrayList<>(game.getPlayers());
            Collections.shuffle(ordered, game.getRng());

            for (int i = 0; i < ordered.size(); i++) {
                ordered.get(i).setOrder(i);
                ordered.get(i).setOffer('\0');
                if(i<1) {
                    ordered.get(i).addFood(2);
                } else if(i<3) {
                    ordered.get(i).addFood(3);
                } else {
                    ordered.get(i).addFood(4);
                }
            }
        }

        // Find current era
        Era currentEra;
        if (boardUninitialized) {
            currentEra = Era.I;
        } else {
            if (board.getTopRowTribe().isEmpty()) {
                currentEra = Era.III;
            } else {
                currentEra = board.getTopRowTribe().getLast().getEra();
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
        SaveGames.saveGame(game.toDTO());
    }
}
