package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.controller.SaveGames;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Game state responsible for the "totem selection" phase.
 * <p>
 * During this phase, each player selects a unique totem in turn order.
 * Once all players have chosen, the game transitions to {@link FillBoardState}
 * and the board is initialized.
 * </p>
 */
public class ChooseTotemState extends GameState {
    private final Game game;
    private List<Player> chooseOrder;

    public ChooseTotemState(Game game) {
        this.game = game;
        chooseOrder = new ArrayList<>(game.getPlayers());

        game.setPlayerTurn(chooseOrder.removeFirst());
    }

    /**
     * Handles the selection of a totem by a player.
     *
     * <p>
     * Enforces turn order and ensures that each totem can only be selected
     * by a single player.
     * Once all players have chosen, the game transitions to {@link FillBoardState},
     * persists the game, and triggers board initialization.
     * </p>
     *
     * @param player the player performing the action
     * @param totem the selected totem
     * @throws IllegalActionException if:
     *         <ul>
     *             <li>it is not the player's turn</li>
     *             <li>the selected totem is already taken</li>
     *         </ul>
     * @throws IOException if an error occurs while saving the game state
     */
    @Override
    public void chooseTotem(Player player, Totem totem) throws IllegalActionException, IOException {
        if (player.equals(game.getPlayerTurn())) {
            if (game.getPlayers().stream().filter(p -> p.getTotem() != null).noneMatch(p -> p.getTotem().equals(totem))) {
                player.setTotem(totem);

                if (!chooseOrder.isEmpty()) {
                    game.setPlayerTurn(chooseOrder.removeFirst());
                } else {
                    game.setState(new FillBoardState(game));
                    SaveGames.saveGame(game.toDTO());
                    game.getState().refillBoard();
                }
            } else {
                throw new IllegalActionException("Player tried to choose a totem that was already taken");
            }
        } else {
            throw new IllegalActionException("Player tried to choose a totem out of order");
        }
    }

    @Override
    public StateDTO getStateDTO() {
        return StateDTO.CHOOSETOTEM;
    }
}