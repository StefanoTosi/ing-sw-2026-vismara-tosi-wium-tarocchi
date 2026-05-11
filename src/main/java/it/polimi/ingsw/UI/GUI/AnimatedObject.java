package it.polimi.ingsw.UI.GUI;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
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
        animatePosition(d, Duration.seconds(0));
    }

    public void animatePosition(Duration d, Duration delay) {
        TranslateTransition trans = new TranslateTransition(d, mesh);
        trans.setInterpolator(Interpolator.EASE_BOTH);

        trans.setFromX(0);
        trans.setToX(reference.localToScene(0, 0).getX() - mesh.getLayoutX());

        trans.setFromY(0);
        trans.setToY(reference.localToScene(0, 0).getY() - mesh.getLayoutY());

        trans.setDelay(delay);

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

    public void flip(Duration delay) {
        RotateTransition rotator = new RotateTransition(Duration.seconds(0.5), mesh);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(mesh.getRotate());
        rotator.setToAngle(mesh.getRotate() + 180);
        rotator.setInterpolator(Interpolator.EASE_BOTH);
        rotator.setDelay(delay);
        rotator.play();
    }

    public void shake() {
        TranslateTransition prev = null;
        int shakes = 4;
        for (int i = 0; i < shakes; i++) {
            int dest = 20;
            if (i == 0 || i == shakes - 1){
                dest = 10;
            }

            if (i % 2 == 1) {
                dest = -dest;
            }

            TranslateTransition trans = new TranslateTransition(Duration.seconds(0.1), mesh);
            trans.setInterpolator(Interpolator.EASE_BOTH);
            trans.setFromX(0);
            trans.setToX(dest);
            if (i != 0) {
                prev.setOnFinished((ActionEvent e) -> {
                    double newLayoutX = mesh.getLayoutX() + mesh.getTranslateX();
                    double newLayoutY = mesh.getLayoutY() + mesh.getTranslateY();

                    mesh.setLayoutX(newLayoutX);
                    mesh.setLayoutY(newLayoutY);

                    mesh.setTranslateX(0);
                    mesh.setTranslateY(0);

                    trans.play();
                });
            } else {
                trans.play();
            }

            if (i == shakes - 1) {
                trans.setOnFinished((ActionEvent e) -> {
                    double newLayoutX = mesh.getLayoutX() + mesh.getTranslateX();
                    double newLayoutY = mesh.getLayoutY() + mesh.getTranslateY();

                    mesh.setLayoutX(newLayoutX);
                    mesh.setLayoutY(newLayoutY);

                    mesh.setTranslateX(0);
                    mesh.setTranslateY(0);
                });
            }
            prev = trans;
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
