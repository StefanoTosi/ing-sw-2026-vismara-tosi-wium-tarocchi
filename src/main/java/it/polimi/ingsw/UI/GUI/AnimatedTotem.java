package it.polimi.ingsw.UI.GUI;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@code AnimateObject} for totems. Has some utility functions to offset their position relative to an offer tile or the order tile
 */
public class AnimatedTotem extends AnimatedObject {
    final int totemW = 245 / 6;
    final int totemH = 427 / 6;

    final List<List<Double>> order2Off = new ArrayList<List<Double>>(Arrays.asList(
            new ArrayList<Double>(Arrays.asList(135.0, 221.0)),
            new ArrayList<Double>(Arrays.asList(135.0, 338.0))
    ));

    final List<List<Double>> order3Off = new ArrayList<List<Double>>(Arrays.asList(
            new ArrayList<Double>(Arrays.asList(135.0, 192.0)),
            new ArrayList<Double>(Arrays.asList(135.0, 310.0)),
            new ArrayList<Double>(Arrays.asList(135.0, 426.0))
    ));

    final List<List<Double>> order4Off = new ArrayList<List<Double>>(Arrays.asList(
            new ArrayList<Double>(Arrays.asList(135.0, 165.0)),
            new ArrayList<Double>(Arrays.asList(135.0, 282.0)),
            new ArrayList<Double>(Arrays.asList(135.0, 398.0)),
            new ArrayList<Double>(Arrays.asList(135.0, 511.0))
    ));

    final List<List<Double>> order5Off = new ArrayList<List<Double>>(Arrays.asList(
            new ArrayList<Double>(Arrays.asList(135.0, 119.0)),
            new ArrayList<Double>(Arrays.asList(135.0, 236.0)),
            new ArrayList<Double>(Arrays.asList(135.0, 352.0)),
            new ArrayList<Double>(Arrays.asList(135.0, 466.0)),
            new ArrayList<Double>(Arrays.asList(135.0, 581.0))
    ));

    final List<Double> offerOff = new ArrayList<Double>(Arrays.asList(125.0, 205.0));

    private double offsetX;
    private double offsetY;

    AnimatedTotem(int i) {
        super(null, null);

        Image img = new Image(getClass().getResource("/totems/" + i + ".png").toExternalForm());
        setMesh(createTotemMesh(img, totemW, totemH));

        Rectangle r = new Rectangle();
        r.setLayoutX(-1000);
        r.setLayoutY(-1000);
        r.setWidth(totemW);
        r.setHeight(totemH);
        r.setFill(Color.TRANSPARENT);
        setReference(r);

        offsetX = 0;
        offsetY = 0;
    }

    @Override
    protected double getRefX() {
        return reference.localToScene(0, 0).getX() + offsetX / 3;
    }

    @Override
    protected double getRefY() {
        return reference.localToScene(0, 0).getY() + offsetY / 3 - totemH;
    }

    /**
     * Creates the mesh for the totem
     * @param front the front texture
     * @param width the width in pixels of the totem
     * @param height the height in pixels of the totem
     * @return the mesh of the totem
     */
    private Group createTotemMesh(Image front, double width, double height) {
        TriangleMesh frontMesh = new TriangleMesh();

        // Define card points
        frontMesh.getPoints().addAll(
                0, 0, 0,  // Point 0 (Top Left)
                (float) width, 0, 0,  // Point 1 (Top Right)
                0, (float) height, 0,  // Point 2 (Bottom Left)
                (float) width, (float) height, 0   // Point 3 (Bottom Right)
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
        frontMat.setDiffuseMap(front);
        frontMeshView.setMaterial(frontMat);
        frontMeshView.setCullFace(CullFace.BACK);

        return new Group(frontMeshView);
    }

    /**
     * Sets the offset of the totem when it is on an offer tile
     */
    public void setOffsetOffer() {
        this.offsetX = offerOff.get(0);
        this.offsetY = offerOff.get(1);
    }

    /**
     * Sets the offset of the totem when it is on an order tile
     * @param i the order on the tile
     * @param numPlayers the number of players of the order tile
     */
    public void setOffsetOrder(int i, int numPlayers) {
        List<List<Double>> orderOff = null;
        switch (numPlayers) {
            case 2:
                orderOff= order2Off;
                break;
            case 3:
                orderOff= order3Off;
                break;
            case 4:
                orderOff= order4Off;
                break;
            case 5:
                orderOff= order5Off;
                break;
        }
        this.offsetX = orderOff.get(i).get(0);
        this.offsetY = orderOff.get(i).get(1);
    }

    public void setImgae(Image img) {
        ((PhongMaterial) ((MeshView) mesh.getChildren().get(0)).getMaterial()).setSelfIlluminationMap(img);
        ((PhongMaterial) ((MeshView) mesh.getChildren().get(0)).getMaterial()).setDiffuseMap(img);
    }
}
