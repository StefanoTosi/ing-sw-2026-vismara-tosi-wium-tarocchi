package it.polimi.ingsw.networking.GUI;

import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.UIObserver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("start-game.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        GUISession.setObserver(fxmlLoader.getController());

        stage.setTitle("Mesos");
        stage.setScene(scene);
        stage.show();
    }
}
