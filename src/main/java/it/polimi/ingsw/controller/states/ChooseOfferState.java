package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ChooseOfferState extends GameState {
    private final Game game;
    private List<Player> drawOrder;

    /**
     * Initialize the state by building the order in which players will be required to choose an offer tile and setting
     * the playerTurn to the first of them
     * @param game
     */
    public ChooseOfferState(Game game) throws IllegalActionException, RemoteException {
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

    public void chooseOffer(Player player, char order) throws IllegalActionException, RemoteException {
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
                }
            } else {
                throw new IllegalActionException("'order' was out of bounds");
            }
        } else {
            throw new IllegalActionException("Player " + player.getName() + " tried to choose offer tile out of order");
        }
    }
}
