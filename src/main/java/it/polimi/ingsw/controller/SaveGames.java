package it.polimi.ingsw.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.PlayerDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.User;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class SaveGames {
    private static Map<String, GameDTO> saves = new HashMap<String, GameDTO>();

    public SaveGames() {}

    public static void saveGame(GameDTO game) throws IOException {
        synchronized (saves) {
            String id = game.getPlayers().getFirst().getName();
            saves.put(id, game);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("saves.json"), saves);
        }
    }

    public static void removeGame(GameDTO game) throws IOException {
        synchronized (saves) {
            String id = game.getPlayers().getFirst().getName();
            saves.remove(id);
            ObjectMapper mapper = new ObjectMapper();
            //create or override a file
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("saves.json"), saves);
        }
    }

    public static void loadSaves(GameController gameController, Map<String, User> users) throws IOException, IllegalActionException {
        ObjectMapper mapper = new ObjectMapper();
        //TypeReference used to not lose generics at run time
        Map<String, GameDTO> saves = mapper.readValue(
                new File("saves.json"),
                new TypeReference<Map<String, GameDTO>>(){}
        );
        for (Map.Entry<String, GameDTO> entry : saves.entrySet()) {
            gameController.addGame(entry.getValue().fromDTO());
            for(PlayerDTO player : entry.getValue().getPlayers()){
                users.get(player.getName()).setInGame(true);
            }
        }
    }
}
