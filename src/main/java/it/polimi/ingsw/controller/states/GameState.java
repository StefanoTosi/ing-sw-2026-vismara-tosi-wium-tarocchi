package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;

/**
 * Abstract base class representing a state in the game state machine.
 * <p>
 * Each concrete subclass defines which actions are valid in that state.
 * By default, all operations throw {@link IllegalActionException} to
 * prevent invalid transitions or actions.
 *
 * <p>
 * This class implements a form of the State design pattern,
 * where behavior is delegated to the current state object inside the game.
 */
public abstract class GameState {
    /**
     * Registers a player in the game (setup states).
     *
     * @param game the game instance
     * @param player the player to register
     * @throws IllegalActionException if the action is not valid in this state
     */
    public void registerPlayer(Game game, Player player) throws IllegalActionException {
        throw new IllegalActionException("Illegal action");
    }

    /**
     * Starts the game (only valid after setup is complete).
     *
     * @param game the game to start
     * @throws IllegalActionException if the action is not valid in this state
     */
    public void startGame(Game game) throws IllegalActionException {
        throw new IllegalActionException("Illegal action");
    }

    /**
     * Allows a player to choose a totem (ChooseTotemState).
     *
     * @param player the player performing the action
     * @param totem the selected totem
     * @throws IllegalActionException if the action is not valid in this state
     * @throws IOException if an I/O error occurs
     */
    public void chooseTotem(Player player, Totem totem) throws IllegalActionException, IOException {
        throw new IllegalActionException("Illegal action");
    }

    /**
     * Refills the board for a new round (FillBoardState).
     *
     * @throws IllegalActionException if the action is not valid in this state
     * @throws IOException if persistence or IO fails
     */
    public void refillBoard() throws IllegalActionException, IOException {
        throw new IllegalActionException("Illegal action");
    }

    /**
     * Allows a player to choose an offer tile (ChooseOfferState).
     *
     * @param player the player performing the action
     * @param order the selected offer identifier
     * @throws IllegalActionException if the action is not valid in this state
     * @throws IOException if an I/O error occurs
     */
    public void chooseOffer(Player player, char order) throws IllegalActionException, IOException {
        throw new IllegalActionException("Illegal action");
    }

    /**
     * Draws a card from the top row (DrawCardState).
     *
     * @param player the player performing the action
     * @param pos the position of the card
     * @throws IllegalActionException if the action is not valid in this state
     * @throws IOException if an I/O error occurs
     */
    public void drawCardFromTop(Player player, int pos) throws IllegalActionException, IOException {
        throw new IllegalActionException("Illegal action");
    }

    /**
     * Draws a card from the bottom row (DrawCardState).
     *
     * @param player the player performing the action
     * @param pos the position of the card
     * @throws IllegalActionException if the action is not valid in this state
     * @throws IOException if an I/O error occurs
     */
    public void drawCardFromBottom(Player player, int pos) throws IllegalActionException, IOException {
        throw new IllegalActionException("Illegal action");
    }

    /**
     * Skips the draw phase if allowed (DrawCardState).
     *
     * @param player the player performing the action
     * @throws IllegalActionException if the action is not valid in this state
     * @throws IOException if an I/O error occurs
     */
    public void skipDraw(Player player) throws IllegalActionException, IOException {
        throw new IllegalActionException("Illegal action");
    }

    /**
     * Resolves all event effects (ResolveEventsState).
     *
     * @throws IllegalActionException if the action is not valid in this state
     * @throws IOException if an I/O error occurs
     */
    public void resolveEvents() throws IllegalActionException, IOException {
        throw new IllegalActionException("Illegal action");
    }

    /**
     * Calculates final rankings and ends the game (EndGameState).
     *
     * @throws IllegalActionException if the action is not valid in this state
     * @throws IOException if an I/O error occurs
     */
    public void calculateRankings() throws IllegalActionException, IOException {
        throw new IllegalActionException("Illegal action");
    }

    public abstract StateDTO getStateDTO();
}