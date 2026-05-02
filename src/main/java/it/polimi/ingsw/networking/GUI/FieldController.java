package it.polimi.ingsw.networking.GUI;

import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.UIObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
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
            Group m = createCardMesh(front, back, cardW, cardH);
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
            Group m = createCardMesh(front, back, cardW, cardH);
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

    @Override
    public void closingGame(GameDTO game) throws IOException, IllegalActionException, ClassNotFoundException, InterruptedException {

    }


    private Group createCardMesh(Image front, Image back, double width, double height) {
        TriangleMesh frontMesh = new TriangleMesh();

        // Define card points
        frontMesh.getPoints().addAll(
                0, 0, 0f,  // Point 0 (Top Left)
                (float) width, 0, 0f,  // Point 1 (Top Right)
                0, (float) height, 0f,  // Point 2 (Bottom Left)
                (float) width, (float) height, 0f   // Point 3 (Bottom Right)
        );

        // Define texture coordinates
        frontMesh.getTexCoords().addAll(
                0f, 0f, // 0: Top Left
                1f, 0f, // 1: Top Right
                0f, 1f, // 2: Bottom Left
                1f, 1f  // 3: Bottom Right
        );

        // Define two faces. Two triangles make a square. Points are counter-clockwise.
        frontMesh.getFaces().addAll(
                0, 0, 2, 2, 1, 1, // Triangle 1
                2, 2, 3, 3, 1, 1  // Triangle 2
        );

        MeshView frontMeshView = new MeshView(frontMesh);

        // Create the materials with no shadows
        PhongMaterial frontMat = new PhongMaterial();
        frontMat.setDiffuseColor(Color.BLACK);
        frontMat.setSelfIlluminationMap(front);
        frontMeshView.setMaterial(frontMat);
        frontMeshView.setCullFace(CullFace.BACK);

        // Build back
        TriangleMesh backMesh = new TriangleMesh();

        // Define card points
        backMesh.getPoints().addAll(
                0, 0, 0f,  // Point 0 (Top Left)
                (float) width, 0, 0f,  // Point 1 (Top Right)
                0, (float) height, 0f,  // Point 2 (Bottom Left)
                (float) width, (float) height, 0f   // Point 3 (Bottom Right)
        );

        // Define texture coordinates
        backMesh.getTexCoords().addAll(
                0f, 0f, // 0: Top Left
                1f, 0f, // 1: Top Right
                0f, 1f, // 2: Bottom Left
                1f, 1f  // 3: Bottom Right
        );

        // Define two faces. Two triangles make a square. Points are counter-clockwise.
        backMesh.getFaces().addAll(
                1, 1, 2, 2, 0, 0, // Triangle 3
                1, 1, 3, 3, 2, 2  // Triangle 4
        );

        MeshView backMeshView = new MeshView(backMesh);

        // Create the materials with no shadows
        PhongMaterial backMat = new PhongMaterial();
        backMat.setDiffuseColor(Color.BLACK);
        backMat.setSelfIlluminationMap(back);
        backMeshView.setMaterial(backMat);
        backMeshView.setCullFace(CullFace.BACK);

        return new Group(frontMeshView, backMeshView);
    }
}
