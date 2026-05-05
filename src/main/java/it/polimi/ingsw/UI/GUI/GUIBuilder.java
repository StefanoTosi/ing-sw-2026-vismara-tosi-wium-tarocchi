package it.polimi.ingsw.UI.GUI;

import it.polimi.ingsw.model.PlayerDTO;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GUIBuilder {
    public static Tab createPlayerTab(PlayerDTO p) {
        Tab t = new Tab(p.getName());
        t.setId(p.getName() + "Tab");

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
        Label pp = new Label("Prestige points: " + p.getPp());
        pp.setId(p.getName() + "Pp");
        Label food = new Label("Food: " + p.getFood());
        food.setId(p.getName() + "Food");
        v.getChildren().add(pp);
        v.getChildren().add(food);
        h.getChildren().add(v);

        ScrollPane s = new ScrollPane();
        s.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        s.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        s.setId(p.getName() + "Scroll");
        h.getChildren().add(s);
        HBox.setHgrow(s, Priority.ALWAYS);

        AnchorPane cards = new AnchorPane();
        s.setContent(cards);

        return t;
    }
}
