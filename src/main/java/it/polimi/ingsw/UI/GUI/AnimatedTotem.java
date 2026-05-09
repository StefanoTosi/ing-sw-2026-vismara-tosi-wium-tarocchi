package it.polimi.ingsw.UI.GUI;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.TriangleMesh;

public class AnimatedTotem extends AnimatedObject {
    final int totemW = 245 / 6;
    final int totemH = 427 / 6;

    AnimatedTotem(int i) {
        super(null, null);

        Image img = new Image(getClass().getResource("/totems/" + i + ".png").toExternalForm());
        setMesh(createTotemMesh(img, totemW, totemH));

        Rectangle r = new Rectangle();
        // r.setLayoutY(offerPathAnim.get(0).getReference().localToScene(0, 0).getX());
        // r.setLayoutY(offerPathAnim.get(0).getReference().localToScene(0, 0).getY());
        r.setLayoutY(0);
        r.setLayoutY(0);
        r.setWidth(totemW);
        r.setHeight(totemH);
        r.setFill(Color.TRANSPARENT);
        // r.setFill(Color.RED);
        setReference(r);
    }

    private Group createTotemMesh(Image front, double width, double height) {
        TriangleMesh frontMesh = new TriangleMesh();

        // Define card points
        frontMesh.getPoints().addAll(
                0, 0, 0.01f,  // Point 0 (Top Left)
                (float) width, 0, 0.01f,  // Point 1 (Top Right)
                0, (float) height, 0.01f,  // Point 2 (Bottom Left)
                (float) width, (float) height, 0.01f   // Point 3 (Bottom Right)
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
        frontMat.setDiffuseMap(front);

        return new Group(frontMeshView);
    }
}
