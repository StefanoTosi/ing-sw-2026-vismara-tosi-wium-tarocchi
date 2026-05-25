package it.polimi.ingsw.UI.GUI;

import it.polimi.ingsw.model.PlayerDTO;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;

/**
 * Utility class that generates dynamically some GUI components
 */
public class GUIBuilder {
    /**
     * Generates the player info card for a certain player
     * @param playerClicked callback execute when the card is clicked
     */
    public static VBox createPlayerCard(PlayerDTO p, EventHandler<MouseEvent> playerClicked) {
        VBox v = new VBox();
        v.setPadding(new Insets(5, 5, 5, 5));
        v.setMinWidth(200);
        v.getStyleClass().add("player-card");
        v.setAlignment(Pos.CENTER);
        v.setMouseTransparent(false);
        v.setOnMouseClicked(playerClicked);

        Label name = new Label(p.getName());
        name.setStyle("-fx-text-fill: #fff;" +
                "-fx-font-size: 16;" +
                "-fx-font-weight: bold;");
        v.getChildren().add(name);

        HBox h = new HBox();
        h.setSpacing(5);
        v.getChildren().add(h);

        // PP indicator
        StackPane sppp = new StackPane();
        Text pp = new Text(p.getPp() + "");
        pp.setFill(Color.WHITE);          // The inside color
        pp.setStroke(Color.BLACK);        // The border color
        pp.setStrokeWidth(3);             // Border thickness
        pp.setStrokeType(StrokeType.OUTSIDE);
        pp.setStyle("-fx-font-size: 40;");
        pp.setId(p.getName() + "Pp");
        ImageView ppi = new ImageView();
        ppi.setImage(new Image(GUIBuilder.class.getResource("/pp.png").toExternalForm()));
        ppi.setFitWidth(100);
        ppi.setFitHeight(100);
        sppp.getChildren().add(ppi);
        sppp.getChildren().add(pp);

        h.getChildren().add(sppp);

        // Food indicator
        StackPane spfood = new StackPane();
        Text food = new Text(p.getFood() + "");
        food.setFill(Color.WHITE);          // The inside color
        food.setStroke(Color.BLACK);        // The border color
        food.setStrokeWidth(3);             // Border thickness
        food.setStrokeType(StrokeType.OUTSIDE);
        food.setId(p.getName() + "Food");
        food.setStyle("-fx-font-size: 40;");
        ImageView foodi = new ImageView();
        foodi.setImage(new Image(GUIBuilder.class.getResource("/food.png").toExternalForm()));
        foodi.setFitWidth(100);
        foodi.setFitHeight(100);
        spfood.getChildren().add(foodi);
        spfood.getChildren().add(food);

        h.getChildren().add(spfood);

        return v;
    }
}
