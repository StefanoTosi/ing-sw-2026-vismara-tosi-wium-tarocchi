package it.polimi.ingsw.UI.GUI;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerDTO;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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

        Label pp = new Label("Prestige points: " + p.getPp());
        pp.setId(p.getName() + "Pp");
        pp.setTextFill(Color.valueOf("#fff"));
        // pp.setStyle("-fx-background-image: url(\"/../../../pp.png\")");
        // pp.getStyleClass().add(".image-label");

        Label food = new Label("Food: " + p.getFood());
        // food.setStyle("-fx-background-image: url(\"/../../../food.png\")");
        // food.getStyleClass().add(".image-label");
        food.setId(p.getName() + "Food");
        food.setTextFill(Color.valueOf("#fff"));
        v.getChildren().add(pp);
        v.getChildren().add(food);
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
}
