package it.polimi.ingsw.networking.GUI;

import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.UIObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FieldController implements UIObserver {
    @FXML HBox topRow;
    @FXML HBox offerPath;
    @FXML HBox bottomRow;

    @FXML Pane cardsContainer;

    final int cardW = 130;
    final int cardH = 200;

    private List<AnimatedCard> topRowAnim;
    private List<AnimatedCard> bottomRowAnim;

    @FXML
    void initialize() {
        topRowAnim = new ArrayList<>();
        bottomRowAnim = new ArrayList<>();

        System.out.println("init");
        Image front = new Image("a.png");
        Image back = new Image("b.png");

        // Place fixed cards in the JavaFx layout
        for (int i = 0; i < GUISession.getGame().getBoard().getTopRowTribe().size(); i++) {
            Rectangle r = new Rectangle(cardW, cardH);
            r.setFill(Color.RED);
            MeshView m = createCardMesh(front, cardW, cardH);
            m.setLayoutX(-cardW);
            m.setLayoutY(-cardH);

            topRow.getChildren().add(r);
            cardsContainer.getChildren().add(m);
            topRowAnim.add(new AnimatedCard(m, r));
        }

        for (int i = 0; i < GUISession.getGame().getBoard().getOfferPath().size(); i++) {
            Rectangle r = new Rectangle(cardW, cardH);
            r.setFill(Color.RED);
            offerPath.getChildren().add(r);
        }

        for (int i = 0; i < GUISession.getGame().getBoard().getBottomRowTribe().size(); i++) {
            Rectangle r = new Rectangle(cardW, cardH);
            r.setFill(Color.RED);
            MeshView m = createCardMesh(front, cardW, cardH);
            m.setLayoutX(-cardW);
            m.setLayoutY(-cardH);

            bottomRow.getChildren().add(r);
            cardsContainer.getChildren().add(m);
            bottomRowAnim.add(new AnimatedCard(m, r));
        }

        // Place animated cards in the cards container
        Platform.runLater(() -> {
            animateAllCards();

            // React to window resizes
            cardsContainer.getScene().widthProperty().addListener((observable, oldValue, newValue) -> {
                resetAllCards();
            });

            cardsContainer.getScene().heightProperty().addListener((observable, oldValue, newValue) -> {
                resetAllCards();
            });
        });

    }

    private void resetAllCards() {
        for (AnimatedCard a : topRowAnim) {
            a.resetPosition();
        }

        for (AnimatedCard a : bottomRowAnim) {
            a.resetPosition();
        }
    }

    private void animateAllCards() {
        for (AnimatedCard a : topRowAnim) {
            a.animatePosition();
        }

        for (AnimatedCard a : bottomRowAnim) {
            a.animatePosition();
        }
    }

    @Override
    public void update(GameDTO game) throws IOException, IllegalActionException {
        GUISession.setGame(game);
    }

    private MeshView createCardMesh(Image image, double width, double height) {
        TriangleMesh mesh = new TriangleMesh();

        // Define card points
        float w = (float) width / 2;
        float h = (float) height / 2;
        mesh.getPoints().addAll(
                0, 0, 0f,  // Point 0 (Top Left)
                (float) width, 0, 0f,  // Point 1 (Top Right)
                0, (float) height, 0f,  // Point 2 (Bottom Left)
                (float) width, (float) height, 0f   // Point 3 (Bottom Right)
        );

        // Define texture coordinates
        mesh.getTexCoords().addAll(
                0f, 0f, // 0: Top Left
                1f, 0f, // 1: Top Right
                0f, 1f, // 2: Bottom Left
                1f, 1f  // 3: Bottom Right
        );

        // Define faces. Two triangles make a square. Points are counter-clockwise.
        mesh.getFaces().addAll(
                0, 0, 2, 2, 1, 1, // Triangle 1
                2, 2, 3, 3, 1, 1  // Triangle 2
        );

        MeshView meshView = new MeshView(mesh);

        // Create the material
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.BLACK);
        material.setSelfIlluminationMap(image);
        meshView.setMaterial(material);
        meshView.setCullFace(CullFace.BACK);

        return meshView;
    }
}
