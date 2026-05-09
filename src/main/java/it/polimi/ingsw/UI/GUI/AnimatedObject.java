package it.polimi.ingsw.UI.GUI;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class AnimatedObject {
    private Group mesh;
    private Rectangle reference;

    AnimatedObject(Group mesh, Rectangle reference) {
        this.mesh = mesh;
        this.reference = reference;
    }

    public void resetPosition() {
        mesh.setLayoutX(reference.localToScene(0, 0).getX());
        mesh.setLayoutY(reference.localToScene(0, 0).getY());
    }

    public void animatePosition(Duration d) {
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
        RotateTransition rotator = new RotateTransition(Duration.seconds(0.7), mesh);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(0);
        rotator.setToAngle(360);
        rotator.setInterpolator(Interpolator.EASE_BOTH);
        rotator.play();
    }

    public void shake() {
        for (int i = 0; i < 4; i++) {
            int dest = 20;
            if (i == 0 || i == 3){
                dest = 10;
            }

            if (i % 2 == 1) {
                dest = -dest;
            }

            TranslateTransition trans = new TranslateTransition(Duration.seconds(0.1), mesh);
            trans.setInterpolator(Interpolator.EASE_BOTH);
            trans.setFromX(0);
            trans.setToX(dest);
            trans.setDelay(Duration.seconds(0.11 * i));
            trans.play();
        }
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
}
