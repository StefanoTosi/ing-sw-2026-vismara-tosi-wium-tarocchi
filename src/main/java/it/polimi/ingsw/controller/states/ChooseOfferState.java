package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

public class ChooseOfferState extends GameState {
    private final Game game;

    public ChooseOfferState(Game game) {
        this.game = game;
        game.setPlayerTurn(0);
    }

    public void chooseOffer(Player player, char order)  throws IllegalActionException {
        if (order >= 'A' && order <= 'G') {
            if (player.equals(game.getPlayers().get(game.getPlayerTurn()))) {
                player.setOrder(order);
                game.setPlayerTurn(0);
                game.incPlayerTurn();
                if (game.getPlayerTurn() >= game.getNumPlayers()) {
                    game.setState(new DrawCardState(game));
                }
            } else {
                throw new IllegalActionException("Player tried to choose offer tile out of order");
            }
        } else {
            throw new IllegalActionException("'order' was out of bounds");
        }
    }
}
