package it.polimi.ingsw.UI.GUI;

import it.polimi.ingsw.UI.UISession;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class starts the GUI by loading the {@code start-game.fxml} scene and attaching it to the {@code Stage}
 */
public class GUIApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load the scene
        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("start-game.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        UISession.setObserver(fxmlLoader.getController());

        // Attach it to the stage
        stage.setTitle("Mesos");
        stage.setScene(scene);
        Image icon = new Image("icon.png");
        stage.getIcons().add(icon);
        stage.show();
    }
}
