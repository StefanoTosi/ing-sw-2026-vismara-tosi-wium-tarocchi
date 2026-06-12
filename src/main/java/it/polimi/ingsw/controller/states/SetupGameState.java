package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;

/**
 * Initial game state responsible for player registration and game initialization.
 * <p>
 * This state collects players until the required number is reached.
 * Once the lobby is full, it automatically starts the game and transitions
 * to {@link ChooseTotemState}.
 * </p>
 */
public class SetupGameState extends GameState {

    /**
     * Registers a player in the game lobby.
     *
     * <p>
     * If the required number of players is reached, the game automatically
     * starts by invoking {@link #startGame(Game)}.
     * </p>
     *
     * @param game the game instance
     * @param player the player joining the game
     * @throws IllegalActionException if registration is invalid in this state
     */
    public void registerPlayer(Game game, Player player) throws IllegalActionException {
        // Check it's a valid action
        game.getPlayers().add(player);

        // Start the game
        if (game.getPlayers().size() == game.getNumPlayers()) {
            game.getState().startGame(game);
        }
    }

    /**
     * Initializes the game once all players have joined.
     *
     * <p>
     * This includes:
     * <ul>
     *     <li>Board initialization</li>
     *     <li>Transition to {@link ChooseTotemState}</li>
     * </ul>
     *
     * @param game the game to start
     */
    public void startGame(Game game) {
        // Initialize board
        game.getBoard().initialize(game.getPlayers().size());
        System.out.println("Game started");

        // Transition
        game.setState(new ChooseTotemState(game));
    }

    public StateDTO getStateDTO() {
        return StateDTO.SETUPGAME;
    }
}
