package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;
import java.util.List;

public abstract class GameState {
    // SetupGameState
    public void registerPlayer(Game game, Player player) throws IllegalActionException, RemoteException {
        throw new IllegalActionException("Illegal action");
    }

    public void startGame(Game game) throws IllegalActionException, RemoteException {
        throw new IllegalActionException("Illegal action");
    }

    // FillBoardState
    public void movePlayersBackToOrder() throws IllegalActionException, RemoteException {
        throw new IllegalActionException("Illegal action");
    }

    public void refillBoard() throws IllegalActionException, RemoteException {
        throw new IllegalActionException("Illegal action");
    }

    // ChooseOfferState
    public void chooseOffer(Player player, char order) throws IllegalActionException, RemoteException {
        throw new IllegalActionException("Illegal action");
    }

    // DrawCardState
    public void drawCardFromTop(Player player, int pos) throws IllegalActionException, RemoteException {
        throw new IllegalActionException("Illegal action");
    }

    public void drawCardFromBottom(Player player, int pos) throws IllegalActionException, RemoteException {
        throw new IllegalActionException("Illegal action");
    }

    // ResoveEventsState
    public void resolveEvents() throws IllegalActionException, RemoteException {
        throw new IllegalActionException("Illegal action");
    }

    // EndTurnState

    // EndGameState
    public void calculateRankings() throws IllegalActionException {
        throw new IllegalActionException("Illegal action");
    }

    public abstract StateDTO getStateDTO() throws IllegalActionException;
}
