package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.Offer;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.characters.Hunter;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.events.Event;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import javafx.util.Pair;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FillBoardState extends GameState {

    private List<Integer> canPick;

    public FillBoardState() {
        this.canPick = new ArrayList<>();
    }

    /*public void movePlayersBackToOrder(Game game) throws IllegalActionException, RemoteException {
        Board board = game.getBoard();

        // Used to initially insert buildings from era I
        boolean boardUninitialized = board.getTopRowTribe().size() == 0 &&
                board.getBottomRowTribe().size() == 0 &&
                board.getTopRowBuilding().size() == 0 &&
                board.getBottomRowBuilding().size() == 0;

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

        // Move players
        for (int i = 0; i < ordered.size(); i++) {
            ordered.get(i).setOrder(i);
            ordered.get(i).setOffer('\0');
        }

        // Resolve end turn effects
        for (Player p : game.getPlayers()) {
            for (Building b : p.getBuildings()) {
                b.getEffect().applyEffectEndTurn(p);
            }
        }

        // Find players who can pick from the top row
        for (int i = 0; i < game.getNumPlayers(); i++) {
            if (game.getPlayers().get(i).getCanPickFromTop()) {
                canPick.add(i);
            }
        }
        if (canPick.size() > 0) {
            game.setPlayerTurn(canPick.remove(0)); // Start from the first one
        } else {
            game.setPlayerTurn(game.getNumPlayers());
        }
    }*/

    /**
     * Refills the top and bottom rows until they are full again. It also moves players back to the order tile and resolves the associated effects
     * @param game the game for which it refills the board
     * @throws IllegalActionException
     */
    public void refillBoard(Game game) throws IllegalActionException, RemoteException {
        Board board = game.getBoard();

        // Used to initially insert buildings from era I
        boolean boardUninitialized = board.getTopRowTribe().size() == 0 &&
                board.getBottomRowTribe().size() == 0 &&
                board.getTopRowBuilding().size() == 0 &&
                board.getBottomRowBuilding().size() == 0;

        // At the beginning of the game, randomly put layers on the order tile
        List<Player> ordered;
        if (boardUninitialized) {
            ordered = new ArrayList<>(game.getPlayers());
            Collections.shuffle(ordered);

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

        // Once done filling, switch to ChooseOfferState
        game.setState(new ChooseOfferState(game));
    }

    /**
     * Draw a card from the top row, not the Event one
     * @param player
     * @param pos
     * @throws IllegalActionException
     */
    /*public void drawCardFromTop(Player player, int pos) throws IllegalActionException {
        Offer offer = player.getGame().getBoard().getOfferPath().get(player.getOffer());
        Game game = player.getGame();

        if (player.equals(game.getPlayers().get(game.getPlayerTurn()))) {
            int index = game.getBoard().getBottomRowTribe().size();
            if (pos < index) {
                Card character= game.getBoard().drawFromTopRowTribe(pos);
                afterDrawn(player, character);
            } else {
                Building building = game.getBoard().drawFromTopRowBuilding(pos-index);
                player.addCard(building);
                building.getEffect().whenDrawn(player);
            }
            if (canPick.size() > 0) {
                game.setPlayerTurn(canPick.remove(0)); // Start from the first one
            } else {
                game.setPlayerTurn(game.getNumPlayers());
            }

            // When all players have draw, transition to ResolveEventsState
            if (game.getPlayerTurn() >= game.getNumPlayers()) {
                // game.setState(new ResolveEventsState(game));
            }
        } else {
            throw new IllegalActionException("Player tried to draw a card out of order or more cards than possible");
        }
    }*/

    /**
     *  Manage the effect applied just after the drawn
     * @param player
     * @param character
     */
    /*private void afterDrawn(Player player, Card character) {
        if (!character.getType().equals("Event")) {
            int tmp_numSets = player.countSets();
            player.addCard(character);
            for (Building building : player.getBuildings()) {
                building.getEffect().applyEffectDraw(player, tmp_numSets, (Character)character);
            }
            if (character.getName().equals("Hunter")) {
                huntersDraft(player, (Hunter) character);
            }
        }
    }*/

    /**
     * Manage the hunters effect
     * @param player
     * @param hunter
     */
    /*private void huntersDraft(Player player, Hunter hunter) {
        if (hunter.getIcon()) {
            player.addFood(player.getNumHunters());
        }
    }*/
}
