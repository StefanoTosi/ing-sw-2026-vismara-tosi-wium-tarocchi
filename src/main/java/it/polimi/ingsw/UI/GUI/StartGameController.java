package it.polimi.ingsw.UI.GUI;

import it.polimi.ingsw.UI.UISession;
import it.polimi.ingsw.controller.states.StateDTO;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.PlayerDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.RMI.ClientRMI;
import it.polimi.ingsw.networking.TCP.ClientTCP;
import it.polimi.ingsw.networking.UIObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.rmi.NotBoundException;

public class StartGameController implements UIObserver {
    @FXML VBox clientSelect;
    @FXML VBox login;
    @FXML VBox newGame;
    @FXML VBox errorToast;
    @FXML VBox waiting;
    @FXML VBox playAgain;

    @FXML Label label;

    @FXML TextField username;
    @FXML PasswordField password;

    @FXML TextField numPlayers;

    /**
     * Loads the CSS for the scene
     */
    @FXML
    void initialize() {
        Platform.runLater(() -> {
            String css = this.getClass().getResource("/it/polimi/ingsw/UI/GUI/style.css").toExternalForm();
            label.getScene().getStylesheets().add(css);
        });
    }

    /**
     * Creates an RMI client
     */
    @FXML
    void selectRMI() throws NotBoundException, IOException {
        UISession.setClient(new ClientRMI(UISession.getObserver(), UISession.getPortRMI(), UISession.getAddr()));
        System.out.println("RMI selected");

        clientSelect.setVisible(false);
        login.setVisible(true);
    }

    /**
     * Creates a TCP client
     */
    @FXML
    void selectTCP() {
        UISession.setClient(new ClientTCP(UISession.getObserver(), UISession.getPortTCP(), UISession.getAddr()));
        System.out.println("TCP selected");

        clientSelect.setVisible(false);
        login.setVisible(true);
    }

    /**
     * Tries to login
     */
    @FXML
    void login() throws IOException, ClassNotFoundException, IllegalActionException, InterruptedException {
        System.out.println(username.getText() + " " + password.getText());

        if (UISession.getClient().addUser(password.getText(), username.getText())) {
            UISession.getClient().ping();
            if (!UISession.getClient().joinGame()) {
                login.setVisible(false);
                newGame.setVisible(true);
                errorToast.setVisible(false);
            } else {
                login.setVisible(false);
                waiting.setVisible(true);
                errorToast.setVisible(false);
                Label l = new Label(username.getText());
                l.getStyleClass().add("waiting-name");
            }
        } else {
            errorToast.setVisible(true);
            ((Label) errorToast.getChildren().getFirst()).setText("Invalid login info");
        }
    }

    /**
     * Tries to create a game
     */
    @FXML
    void createGame() {
        try {
            int num = Integer.parseInt(numPlayers.getText());
            if (num  < 2 || num > 5) {
                errorToast.setVisible(true);
                ((Label) errorToast.getChildren().getFirst()).setText("Number of players must be between 2 and 5");
            } else {
                UISession.getClient().createGame(num);
                newGame.setVisible(false);
                waiting.setVisible(true);
                errorToast.setVisible(false);
            }
        } catch (Exception e) {
            errorToast.setVisible(true);
            ((Label) errorToast.getChildren().getFirst()).setText("Invalid number of players");
        }
    }

    /**
     * Tries to enter a new match when kicked out
     */
    @FXML
    void playAgain() {
        try {
            if (!UISession.getClient().joinGame()) {
                playAgain.setVisible(false);
                newGame.setVisible(true);
                errorToast.setVisible(false);
            } else {
                playAgain.setVisible(false);
                waiting.setVisible(true);
                errorToast.setVisible(false);
                Label l = new Label(username.getText());
                l.getStyleClass().add("waiting-name");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Exits
     */
    @FXML
    void quit() {
        System.exit(0);
    }

    /**
     * Once all players have joined, loads the field scene
     */
    @Override
    public void update(GameDTO game) throws IOException, IllegalActionException {
        UISession.setGame(game);
        Platform.runLater(() -> {
            if (game.getState() != StateDTO.SETUPGAME) {
                FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("field.fxml"));
                Parent fieldRoot = null;

                try {
                    fieldRoot = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                label.getScene().setRoot(fieldRoot);
                UISession.setObserver(fxmlLoader.getController());
            }
        });
    }

    /**
     * Handles the game closing
     */
    @Override
    public void closingGame(GameDTO game) throws IOException, IllegalActionException, ClassNotFoundException, InterruptedException {
        // TODO: !!!
    }

    /**
     * Handles a server crash
     */
    @Override
    public void serverCrashed() {
        System.out.println("Sorry, the server crashed\n");
        System.exit(1);
    }
}
