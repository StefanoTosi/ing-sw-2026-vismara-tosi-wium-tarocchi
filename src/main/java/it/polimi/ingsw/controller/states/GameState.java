package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

public abstract class GameState {
    // SetupGameState
    public void registerPlayer(Game game, String name) throws IllegalActionException {
        throw new IllegalActionException("Illegal action");
    }

    public void startGame(Game game) throws IllegalActionException {
        throw new IllegalActionException("Illegal action");
    }

    // FillBoardState
    public void refillBoard(Game game) throws IllegalActionException {
        throw new IllegalActionException("Illegal action");
    }

    // ChooseOfferState
    public void chooseOffer(Player player, char order) throws IllegalActionException {
        throw new IllegalActionException("Illegal action");
    }

    // DrawCardState
    public void drawCardFromTop(Player player, int pos) throws IllegalActionException {
        throw new IllegalActionException("Illegal action");
    }

    public void drawCardFromBottom(Player player, int pos) throws IllegalActionException {
        throw new IllegalActionException("Illegal action");
    }

}
