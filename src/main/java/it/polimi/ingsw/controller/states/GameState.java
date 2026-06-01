package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;

public abstract class GameState {
    // SetupGameState
    public void registerPlayer(Game game, Player player) throws IllegalActionException {
        throw new IllegalActionException("Illegal action");
    }

    public void startGame(Game game) throws IllegalActionException {
        throw new IllegalActionException("Illegal action");
    }

    // ChooseTotemState
    public void chooseTotem(Player player, Totem totem) throws IllegalActionException, IOException {
        throw new IllegalActionException("Illegal action");
    }

    // FillBoardState
    public void refillBoard() throws IllegalActionException, IOException {
        throw new IllegalActionException("Illegal action");
    }

    // ChooseOfferState
    public void chooseOffer(Player player, char order) throws IllegalActionException, IOException {
        throw new IllegalActionException("Illegal action");
    }

    // DrawCardState
    public void drawCardFromTop(Player player, int pos) throws IllegalActionException, IOException {
        throw new IllegalActionException("Illegal action");
    }

    public void drawCardFromBottom(Player player, int pos) throws IllegalActionException, IOException {
        throw new IllegalActionException("Illegal action");
    }

    public void skipDraw(Player player) throws IllegalActionException, IOException {
        throw new IllegalActionException("Illegal action");
    }
    // ResolveEventsState
    public void resolveEvents() throws IllegalActionException, IOException {
        throw new IllegalActionException("Illegal action");
    }

    // EndGameState
    public void calculateRankings() throws IllegalActionException, IOException {
        throw new IllegalActionException("Illegal action");
    }

    public abstract StateDTO getStateDTO();
}