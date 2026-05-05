package it.polimi.ingsw.UI.GUI;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.CardDTO;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.List;
import java.util.function.Consumer;

public class AnimatedCard {
    private Group mesh;
    private Rectangle reference;
    private CardDTO card;

    final int cardW = 575 / 5;
    final int cardH = 810 / 5;

    public AnimatedCard(CardDTO card, EventHandler<MouseEvent> clickHandler) {
        this.card = card;

        // Load images
        Image front = new Image(getClass().getResource("/front/" + card.getId() + ".png").toExternalForm());
        Image back = new Image(getClass().getResource("/back/" + card.getId() + ".png").toExternalForm());

        // Create reference
        reference = new Rectangle(cardW, cardH);
        reference.setFill(Color.RED);

        mesh = createCardMesh(front, back, cardW, cardH);
        mesh.setLayoutX(-cardW);
        mesh.setLayoutY(-cardH);
        mesh.setOnMouseClicked(clickHandler);
    }

    public Group getMesh() {
        return mesh;
    }

    public void setMesh(Group mesh) {
        this.mesh = mesh;
    }

    public Rectangle getReference() {
        return reference;
    }

    public void setReference(Rectangle reference) {
        this.reference = reference;
    }

    public CardDTO getCard() {
        return card;
    }

    public void setCard(CardDTO card) {
        this.card = card;
    }

    public void resetPosition() {
        mesh.setLayoutX(reference.localToScene(0, 0).getX());
        mesh.setLayoutY(reference.localToScene(0, 0).getY());
    }

    public void animatePosition(Duration d) {
        mesh.toFront();
        TranslateTransition trans = new TranslateTransition(d, mesh);
        trans.setInterpolator(Interpolator.EASE_BOTH);

        trans.setFromX(0);
        trans.setToX(reference.localToScene(0, 0).getX() - mesh.getLayoutX());

        trans.setFromY(0);
        trans.setToY(reference.localToScene(0, 0).getY() - mesh.getLayoutY());

        trans.setOnFinished(event -> {
            double newLayoutX = mesh.getLayoutX() + mesh.getTranslateX();
            double newLayoutY = mesh.getLayoutY() + mesh.getTranslateY();

            mesh.setLayoutX(newLayoutX);
            mesh.setLayoutY(newLayoutY);

            mesh.setTranslateX(0);
            mesh.setTranslateY(0);
        });
        trans.play();

        // 3D flip
        /*RotateTransition rotator = new RotateTransition(Duration.seconds(2), mesh);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(180);
        rotator.setToAngle(0);
        rotator.setDelay(Duration.seconds(0.2));
        rotator.setInterpolator(Interpolator.EASE_BOTH);
        rotator.play();*/
    }

    public void animatePosition(Duration d, double x, double y) {
        mesh.toFront();
        TranslateTransition trans = new TranslateTransition(d, mesh);
        trans.setInterpolator(Interpolator.EASE_BOTH);

        trans.setFromX(0);
        trans.setToX(x - mesh.getLayoutX());

        trans.setFromY(0);
        trans.setToY(y - mesh.getLayoutY());

        trans.setOnFinished(event -> {
            double newLayoutX = mesh.getLayoutX() + mesh.getTranslateX();
            double newLayoutY = mesh.getLayoutY() + mesh.getTranslateY();

            mesh.setLayoutX(newLayoutX);
            mesh.setLayoutY(newLayoutY);

            mesh.setTranslateX(0);
            mesh.setTranslateY(0);
        });
        trans.play();
    }

    public void spin() {
        RotateTransition rotator = new RotateTransition(Duration.seconds(0.4), mesh);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(0);
        rotator.setToAngle(360);
        rotator.setInterpolator(Interpolator.EASE_BOTH);
        rotator.play();
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
                0.1f, 0.07f, // 0: Top Left
                0.89f, 0.07f, // 1: Top Right
                0.1f, 0.92f, // 2: Bottom Left
                0.89f, 0.92f  // 3: Bottom Right
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
                0.1f, 0.07f, // 0: Top Left
                0.89f, 0.07f, // 1: Top Right
                0.1f, 0.92f, // 2: Bottom Left
                0.89f, 0.92f  // 3: Bottom Right
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
