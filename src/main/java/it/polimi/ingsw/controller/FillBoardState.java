package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Building;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.events.Event;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.util.ArrayList;

public class FillBoardState extends GameState {

    /**
     * Refills the top and bottom rows until they are full again
     * @param game the game for which it refills the board
     * @throws IllegalActionException
     */
    public void refillBoard(Game game) throws IllegalActionException {
        Board b = game.getBoard();

        // Used to initially insert buildings from era I
        boolean boardUninitialized =    b.getTopRowTribe().size() == 0 &&
                                        b.getBottomRowTribe().size() == 0 &&
                                        b.getTopRowBuilding().size() == 0 &&
                                        b.getBottomRowBuilding().size() == 0;

        // Find current era
        Era currentEra;
        if (boardUninitialized) {
            currentEra = Era.I;
        } else {
            currentEra = b.getTopRowTribe().get(b.getTopRowTribe().size() - 1).getEra();
        }

        // Clear and move rows
        b.getBottomRowTribe().clear();
        b.setBottomRowTribe(new ArrayList<>(b.getTopRowTribe()));

        b.getTopRowTribe().clear();

        // Fill bottom row if board is uninitialized
        if (boardUninitialized) {
            while (b.getDeckTribe().size() > 0 && b.getBottomRowTribe().size() < game.getNumPlayers() + 1) {
                Card c = b.getDeckTribe().draw();

                if (c instanceof Event) {
                    b.getTopRowTribe().add(c);
                } else {
                    b.getBottomRowTribe().add(c);
                }
            }
        }

        // Fill top row
        while (b.getDeckTribe().size() > 0 && b.getTopRowTribe().size() < game.getNumPlayers() + 4) {
            Card c = b.getDeckTribe().draw();

            // Begin new era
            if (c.getEra() != currentEra) {
                currentEra = c.getEra();

                b.getBottomRowBuilding().clear();
                b.setBottomRowBuilding(new ArrayList<>(b.getTopRowBuilding()));

                b.getTopRowBuilding().clear();
                switch (currentEra) {
                    case Era.II:
                        while (b.getDeckE2Building().size() > 0) {
                            b.getTopRowBuilding().add((Building) b.getDeckE2Building().draw());
                        }
                        break;

                    case Era.III:
                        while (b.getDeckE3Building().size() > 0) {
                            b.getTopRowBuilding().add((Building) b.getDeckE3Building().draw());
                        }
                        break;

                    default:
                        throw new IllegalActionException("Invalid 'currentEra'");
                }
            }

            b.getTopRowTribe().add(c);
        }

        if (boardUninitialized) {
            // Place all buildings from era I
            while (b.getDeckE1Building().size() > 0) {
                b.getTopRowBuilding().add((Building) b.getDeckE1Building().draw());
            }
        }

        // Once done filling, switch to ChooseOfferState
        game.setState(new ChooseOfferState(game));
    }
}
