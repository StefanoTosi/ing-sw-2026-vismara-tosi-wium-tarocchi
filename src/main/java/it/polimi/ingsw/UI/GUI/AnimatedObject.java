package it.polimi.ingsw.UI.GUI;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * This is a class that simplifies animating {@code MeshView}s, by holding a reference to both
 * a {@code Group} containing the mesh and an invisible reference {@code Rectangle} object that gets updated by the JavaFX layout
 */
public class AnimatedObject {
    protected Group mesh;
    protected Rectangle reference;

    AnimatedObject(Group mesh, Rectangle reference) {
        this.mesh = mesh;
        this.reference = reference;
    }

    protected double getRefX() {
        return reference.localToScene(0, 0).getX();
    }

    protected double getRefY() {
        return reference.localToScene(0, 0).getY();
    }

    /**
     * Instantly aligns the position of the mesh to the position of the reference
     */
    public void resetPosition() {
        mesh.setLayoutX(getRefX());
        mesh.setLayoutY(getRefY());
    }

    /**
     * Animates the position of the mesh to the position of the reference
     * @param d the duration of the animation
     */
    public void animatePosition(Duration d) {
        animatePosition(d, Duration.seconds(0));
    }

    /**
     * Animates the position of the mesh to the position of the reference with some delay
     * @param d the duration of the animation
     * @param delay the delay of the animation
     */
    public void animatePosition(Duration d, Duration delay) {
        TranslateTransition trans = new TranslateTransition(d, mesh);
        trans.setInterpolator(Interpolator.EASE_BOTH);

        trans.setFromX(0);
        trans.setToX(getRefX() - mesh.getLayoutX());

        trans.setFromY(0);
        trans.setToY(getRefY() - mesh.getLayoutY());

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

    /**
     * Animates the mesh with a 3D vertical spin
     */
    public void spin() {
        RotateTransition rotator = new RotateTransition(Duration.seconds(0.7), mesh);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(0);
        rotator.setToAngle(360);
        rotator.setInterpolator(Interpolator.EASE_BOTH);
        rotator.play();
    }

    /**
     * Animates the mesh with a 3D vertical half flip
     * @param delay the delay of the animation
     */
    public void flip(Duration delay) {
        RotateTransition rotator = new RotateTransition(Duration.seconds(0.5), mesh);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(mesh.getRotate());
        rotator.setToAngle(mesh.getRotate() + 180);
        rotator.setInterpolator(Interpolator.EASE_BOTH);
        rotator.setDelay(delay);
        rotator.play();
    }

    /**
     * Animates the mesh with a shake
     */
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
