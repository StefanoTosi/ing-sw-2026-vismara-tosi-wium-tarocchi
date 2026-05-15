package it.polimi.ingsw.UI.GUI;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerDTO;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;

import java.awt.font.ImageGraphicAttribute;

public class GUIBuilder {
    public static Tab createPlayerTab(PlayerDTO p) {
        Tab t = new Tab(p.getName());
        t.setId(p.getName() + "Tab");
        t.setStyle("-fx-background-color:  #171717; -fx-border-color:  #F04D3B; -fx-border-width:  2;  ");
        String spacingStyle =
            "-fx-border-color: #000; " +
            "-fx-border-width: 2; " +
            "-fx-border-insets: 0 5 0 0; " +      // Push the border 5px from the right
            "-fx-background-insets: 0 5 0 0; " +  // Push the background 5px from the right
            "-fx-background-color: #171717;" +
            "-fx-border-radius:  5;" +
            "-fx-background-radius:  6;" +
            "-fx-text-base-color: #fff;" +
            "-fx-focus-color: transparent;" +
            "-fx-faint-focus-color: transparent;";
        t.setStyle(spacingStyle);


        AnchorPane a = new AnchorPane();
        // a.setMinHeight(0);
        t.setContent(a);

        HBox h = new HBox();
        a.getChildren().add(h);
        AnchorPane.setBottomAnchor(h, 0.0);
        AnchorPane.setTopAnchor(h, 0.0);
        AnchorPane.setRightAnchor(h, 0.0);
        AnchorPane.setLeftAnchor(h, 0.0);

        VBox v = new VBox();
        v.setPadding(new Insets(5, 5, 5, 5));
        v.setMinWidth(200);
        v.setSpacing(5);

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

        StackPane spfood = new StackPane();
        Text food = new Text(p.getFood() + "");
        // food.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
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

        v.getChildren().add(sppp);
        v.getChildren().add(spfood);
        h.getChildren().add(v);

        ScrollPane s = new ScrollPane();
        s.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        s.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        s.setStyle("-fx-background-color:  #171717");
        h.getChildren().add(s);
        HBox.setHgrow(s, Priority.ALWAYS);

        AnchorPane sa = new AnchorPane();
        s.setContent(sa);

        HBox cards = new HBox();
        cards.setId(p.getName() + "Hand");
        cards.setSpacing(5);
        cards.setAlignment(Pos.CENTER_LEFT);
        cards.setFillHeight(true);
        cards.setStyle("-fx-background-color: #00000000");
        sa.getChildren().add(cards);
        AnchorPane.setBottomAnchor(cards, 0.0);
        AnchorPane.setTopAnchor(cards, 0.0);
        AnchorPane.setRightAnchor(cards, 0.0);
        AnchorPane.setLeftAnchor(cards, 0.0);

        return t;
    }
    public static VBox createPlayerCard(PlayerDTO p) {
        VBox v = new VBox();
        v.setPadding(new Insets(5, 5, 5, 5));
        v.setMinWidth(200);
        v.getStyleClass().add("player-card");
        v.setAlignment(Pos.CENTER);
        v.setMouseTransparent(false);

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
