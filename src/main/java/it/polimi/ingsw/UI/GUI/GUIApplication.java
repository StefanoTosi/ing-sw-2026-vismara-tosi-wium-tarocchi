package it.polimi.ingsw.UI.GUI;

import it.polimi.ingsw.UI.UISession;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("start-game.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        UISession.setObserver(fxmlLoader.getController());

        /*PerspectiveCamera camera = new PerspectiveCamera();
        scene.setCamera(camera);*/

        stage.setTitle("Mesos");
        stage.setScene(scene);
        Image icon = new Image("icon.png");
        stage.getIcons().add(icon);
        // scene.getRoot().setStyle("-fx-background-image: url('/cover.png'); -fx-background-size: 100% 100%; -fx-background-repeat: no-repeat;");
        stage.show();
    }
}
