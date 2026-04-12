package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.ClientCallBack;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    private List<Game> games;

    public GameController() {
        games = new ArrayList<>();
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public List<Game> getGames() {
        return games;
    }

    public Player getPlayer(String nickname) throws RemoteException {
        for(Game game : games){
            for(Player player : game.getPlayers()){
                if(player.getName().equals(nickname)){
                    return player;
                }
            }
        }
        return null;
    }

    public void addGame(Game game) throws IllegalActionException {
        games.add(game);
    }

    public void removeGame(Game game) throws IllegalActionException {
        games.remove(game);
    }

    public boolean joinGame(Player player, ClientCallBack client) throws RemoteException, IllegalActionException {
        for(Game game : games){
            if(game.getPlayers().size() < game.getNumPlayers()){
                game.addObserver(client);
                player.setGame(game);
                game.getState().registerPlayer(game, player);
                return true;
            }
        }
        return false;
    }

    public void createGame(Player player, int num, ClientCallBack client) throws RemoteException, IllegalActionException {
        Game newGame = new Game(new ArrayList<Player>());
        addGame(newGame);
        newGame.setNumPlayers(num);
        newGame.addObserver(client);
        player.setGame(newGame);
        newGame.getState().registerPlayer(newGame, player);
    }

    public void executeAction(Action action, String player) throws IllegalArgumentException, IllegalActionException, RemoteException {
        action. execute(getPlayer(player));
    }
}
