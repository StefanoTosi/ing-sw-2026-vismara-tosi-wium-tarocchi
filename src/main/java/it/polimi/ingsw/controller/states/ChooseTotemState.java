package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.controller.SaveGames;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChooseTotemState extends GameState {
    private final Game game;
    private List<Player> chooseOrder;

    public ChooseTotemState(Game game) {
        this.game = game;
        chooseOrder = new ArrayList<>(game.getPlayers());

        game.setPlayerTurn(chooseOrder.removeFirst());
    }

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
                throw new IllegalActionException("Player " + player.getName() + " tried to choose a totem that was already taken");
            }
        } else {
            throw new IllegalActionException("Player " + player.getName() + " tried to choose a totem out of order");
        }
    }

    @Override
    public StateDTO getStateDTO() throws IllegalActionException {
        return StateDTO.CHOOSETOTEM;
    }
}
