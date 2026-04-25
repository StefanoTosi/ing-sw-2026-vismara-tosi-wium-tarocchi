package it.polimi.ingsw.networking.GUI;

import it.polimi.ingsw.controller.states.StateDTO;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.Player;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.rmi.NotBoundException;

public class StartGameController implements UIObserver {
    @FXML VBox clientSelect;
    @FXML VBox login;
    @FXML VBox newGame;
    @FXML VBox errorToast;
    @FXML VBox waiting;

    @FXML Label label;

    @FXML TextField username;
    @FXML PasswordField password;

    @FXML TextField numPlayers;

    @FXML
    void selectRMI() throws NotBoundException, IOException {
        GUISession.setClient(new ClientRMI(GUISession.getObserver(), GUISession.getPortRMI(), GUISession.getAddr()));
        System.out.println("RMI selected");

        clientSelect.setVisible(false);
        login.setVisible(true);
    }

    @FXML
    void selectTCP() {
        GUISession.setClient(new ClientTCP(GUISession.getObserver(), GUISession.getPortTCP(), GUISession.getAddr()));
        System.out.println("TCP selected");

        clientSelect.setVisible(false);
        login.setVisible(true);
    }

    @FXML
    void login() throws IOException, ClassNotFoundException, IllegalActionException {
        System.out.println(username.getText() + " " + password.getText());

        if (GUISession.getClient().addUser(password.getText(), username.getText()) == 0) {
            if (!GUISession.getClient().joinGame()) {
                login.setVisible(false);
                newGame.setVisible(true);
                errorToast.setVisible(false);
            } else {
                login.setVisible(false);
                waiting.setVisible(true);
                errorToast.setVisible(false);
                waiting.getChildren().add(new Label(username.getText()));
            }
        } else {
            errorToast.setVisible(true);
            ((Label) errorToast.getChildren().get(0)).setText("Invalid login info");
        }
    }

    @FXML void createGame() {
        try {
            int num = Integer.parseInt(numPlayers.getText());
            if (num  < 2 || num > 5) {
                errorToast.setVisible(true);
                ((Label) errorToast.getChildren().get(0)).setText("Number of players must be between 2 and 5");
            } else {
                GUISession.getClient().createGame(num);
                newGame.setVisible(false);
                waiting.setVisible(true);
                errorToast.setVisible(false);

                waiting.getChildren().add(new Label(username.getText()));
            }
        } catch (Exception e) {
            errorToast.setVisible(true);
            ((Label) errorToast.getChildren().get(0)).setText("Invalid number of players");
        }
    }

    @Override
    public void update(GameDTO game) throws IOException, IllegalActionException {
        GUISession.setGame(game);
        Platform.runLater(() -> {
            for (PlayerDTO p : game.getPlayers()) {
                if (waiting.getChildren()
                        .stream()
                        .map(n -> (Label) n)
                        .filter(l -> l.getText().equals(p.getName()))
                        .count() == 0
                ) {
                    waiting.getChildren().add(new Label(p.getName()));
                }
            }

            if (game.getState() != StateDTO.SETUPGAME) {
                FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("field.fxml"));
                Parent fieldRoot = null;
                try {
                    fieldRoot = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                label.getScene().setRoot(fieldRoot);
                GUISession.setObserver(fxmlLoader.getController());
            }
        });
    }
}
