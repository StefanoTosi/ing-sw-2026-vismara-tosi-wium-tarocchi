package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.controller.SaveGames;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Game state responsible for handling the "offer selection" phase.
 * <p>
 * During this state, players select their offer tile in turn order.
 * Once all players have chosen, the game transitions to {@link DrawCardState}.
 * </p>
 */
public class ChooseOfferState extends GameState {
    private final Game game;
    private List<Player> drawOrder;

    /**
     * Initializes the state and computes the order in which players will
     * select their offer tiles.
     *
     * <p>
     * Players are sorted by their turn order and queued for selection.
     * The first player in the queue is immediately set as the current turn.
     * </p>
     *
     * @param game the game associated with this state
     */
    public ChooseOfferState(Game game) {
        this.game = game;
        drawOrder = new ArrayList<>(game.getPlayers()
                        .stream()
                        .sorted((p1, p2) -> p1.getOrder() - p2.getOrder())
                        .toList());

        game.setPlayerTurn(drawOrder.removeFirst());
    }

    public StateDTO getStateDTO() {
        return StateDTO.CHOOSEOFFER;
    }

    /**
     * Handles the selection of an offer tile by a player.
     *
     * <p>
     * The method enforces turn order and validates the chosen offer.
     * Once all players have selected their offers, the game transitions
     * to {@link DrawCardState} and the game is persisted.
     * </p>
     *
     * @param player the player performing the action
     * @param order the identifier of the chosen offer tile
     * @throws IllegalActionException if:
     *         <ul>
     *             <li>it is not the player's turn</li>
     *             <li>the selected offer identifier is invalid</li>
     *         </ul>
     * @throws IOException if an error occurs while saving the game state
     */
    public void chooseOffer(Player player, char order) throws IllegalActionException, IOException {
        if (player.equals(game.getPlayerTurn())) {
            if (order >= 'A' && order <= 'G') {
                player.setOffer(order);

                // Increment player turn or go to DrawCardsState
                if (!drawOrder.isEmpty()) {
                    game.setPlayerTurn(drawOrder.removeFirst());
                } else {
                    System.out.println("Finished choosing offer tiles");
                    game.setPlayerTurn(null);
                    game.setState(new DrawCardState(game));
                    SaveGames.saveGame(game.toDTO());
                }
            } else {
                throw new IllegalActionException("'order' was out of bounds");
            }
        } else {
            throw new IllegalActionException("Player tried to choose offer tile out of order");
        }
    }
}
