package it.polimi.ingsw.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.controller.states.StateDTO;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.PlayerDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.User;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class used to manage game persistence.<br>
 * This class allows:<br>
 *    - Saving active games to a JSON file<br>
 *    - Removing saved games<br>
 *    - Loading saved games after a server restart<br>
 * Games are stored as {@link GameDTO} objects serialized through Jackson.
 */
public class SaveGames {
    private static Map<String, GameDTO> saves = new HashMap<String, GameDTO>();

    /**
     * Default constructor.
     */
    public SaveGames() {}

    /**
     * Saves or updates a game inside the JSON save file.
     *
     * @param game the game DTO to save
     * @throws IOException if an error occurs while writing the file
     */
    public static void saveGame(GameDTO game) throws IOException {
        synchronized (saves) {
            // Use first player's name as unique game identifier
            String id = game.getPlayers().getFirst().getName();
            // Add or overwrite the save in memory
            saves.put(id, game);

            ObjectMapper mapper = new ObjectMapper();
            // Serialize the entire save map into the JSON file
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/it/polimi/ingsw/saves.json"), saves);
        }
    }

    /**
     * Removes a saved game from the save file.
     *
     * @param game the game DTO to remove
     * @throws IOException if an error occurs while writing the file
     */
    public static void removeGame(GameDTO game) throws IOException {
        synchronized (saves) {
            String id = game.getPlayers().getFirst().getName();
            saves.remove(id);
            ObjectMapper mapper = new ObjectMapper();
            //create or override a file
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/it/polimi/ingsw/saves.json"), saves);
        }
    }

    /**
     * Loads all saved games from disk and restores them into the game controller.
     * This method also restores user states by marking players as currently in-game.
     *
     * @param gameController the controller where games are restored
     * @param users the server user map
     * @throws IOException if the save file cannot be read
     * @throws IllegalActionException if restoring a game produces an invalid state
     */
    public static void loadSaves(GameController gameController, Map<String, User> users) throws IOException, IllegalActionException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/resources/it/polimi/ingsw/saves.json");
        //TypeReference used to not lose generics at run time
        // Check that the file exists and is not empty
        if(file.exists() && file.length() != 0) {
            // Deserialize the JSON save file into a map of GameDTOs
            Map<String, GameDTO> saves = mapper.readValue(
                    file,
                    new TypeReference<Map<String, GameDTO>>(){}
            );
            // Iterate over every saved game
            for (Map.Entry<String, GameDTO> entry : saves.entrySet()) {
                StateDTO state = entry.getValue().getState();
                Game game = entry.getValue().fromDTO();
                // Restore the correct runtime state object
                game.setState(state.getNewState(game));
                gameController.addGame(game);
                boolean flag = users.isEmpty();
                // Restore all players as "in game"
                for(PlayerDTO player : entry.getValue().getPlayers()){
                    if(!flag){
                        // If DataBase online
                        users.get(player.getName()).setInGame(true);
                    }else{
                        // Create a temporary user object if missing
                        // If the database is off
                        User user = new User(player.getName());
                        user.setInGame(true);
                        users.put(player.getName(), user);
                    }
                }
            }
        }
    }
}
