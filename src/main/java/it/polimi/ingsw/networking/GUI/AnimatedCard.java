package it.polimi.ingsw.networking.GUI;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class AnimatedCard {
    private MeshView mesh;
    private Rectangle reference;

    public AnimatedCard(MeshView mesh, Rectangle reference) {
        this.mesh = mesh;
        this.reference = reference;
    }

    public MeshView getMesh() {
        return mesh;
    }

    public void setMesh(MeshView mesh) {
        this.mesh = mesh;
    }

    public Rectangle getReference() {
        return reference;
    }

    public void setReference(Rectangle reference) {
        this.reference = reference;
    }

    public void resetPosition() {
        mesh.setLayoutX(reference.localToScene(0, 0).getX());
        mesh.setLayoutY(reference.localToScene(0, 0).getY());
    }

    public void animatePosition() {
        TranslateTransition trans = new TranslateTransition(Duration.seconds(0.2), mesh);
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
    }
}
