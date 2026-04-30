package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.actions.Action;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.RMI.ClientCallBack;
import it.polimi.ingsw.networking.TCP.ObserverTCP;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public boolean joinGameRMI(Player player, ClientCallBack client) throws IOException, IllegalActionException, InterruptedException {
        for(Game game : games){
            if(game.getPlayers().size() < game.getNumPlayers()){
                game.addObserverRMI(client);
                player.setGame(game);
                game.getState().registerPlayer(game, player);
                game.notifyObserver();
                return true;
            }
        }
        return false;
    }

    public void createGameRMI(Player player, int num, ClientCallBack client) throws RemoteException, IllegalActionException {
        Game newGame = new Game(new ArrayList<Player>(), new Random(System.currentTimeMillis()));
        addGame(newGame);
        newGame.setNumPlayers(num);
        newGame.addObserverRMI(client);
        player.setGame(newGame);
        newGame.getState().registerPlayer(newGame, player);
    }

    public boolean joinGameTCP(Player player, ObserverTCP client) throws Exception {
        for(Game game : games){
            if(game.getPlayers().size() < game.getNumPlayers()){
                game.addObserverTCP(client);
                player.setGame(game);
                game.getState().registerPlayer(game, player);
                game.notifyObserver();
                return true;
            }
        }
        return false;
    }

    public void createGameTCP(Player player, int num, ObserverTCP client) throws RemoteException, IllegalActionException {
        Game newGame = new Game(new ArrayList<Player>(), new Random(System.currentTimeMillis()));
        addGame(newGame);
        newGame.setNumPlayers(num);
        newGame.addObserverTCP(client);
        player.setGame(newGame);
        newGame.getState().registerPlayer(newGame, player);
    }

    public void executeAction(Action action, String player) throws IllegalArgumentException, IllegalActionException, IOException, InterruptedException {
        getPlayer(player).getGame().setErrorFlag("");
        try {
            action.execute(getPlayer(player));
        } catch (IllegalActionException e) {
            getPlayer(player).getGame().setErrorFlag(e.getReason());
        }
        getPlayer(player).getGame().notifyObserver();
    }
}