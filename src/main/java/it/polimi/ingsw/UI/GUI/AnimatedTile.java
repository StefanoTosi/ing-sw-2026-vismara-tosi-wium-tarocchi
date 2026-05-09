package it.polimi.ingsw.UI.GUI;

import it.polimi.ingsw.model.CardDTO;
import it.polimi.ingsw.model.board.OfferDTO;
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

import java.lang.ref.Reference;

public class AnimatedTile extends AnimatedObject {
    private OfferDTO tile;

    public final static int tileW = 391 / 3;
    public final static int tileH = 627 / 3;

    public AnimatedTile(OfferDTO tile, HBox path, Pane cardsContainer, EventHandler<MouseEvent> clickHandler) {
        this.tile = tile;
        super(null, null);

        // Load images
        Image front = new Image(getClass().getResource("/offer/" + tile.getOrder() + ".png").toExternalForm());

        // Create reference
        Rectangle reference = new Rectangle(tileW, tileH);
        reference.setFill(Color.TRANSPARENT);
        setReference(reference);

        Group mesh = createTileMesh(front, tileW, tileH);
        mesh.setOnMouseClicked(clickHandler);
        setMesh(mesh);
        resetPosition();

        path.getChildren().add(reference);
        cardsContainer.getChildren().add(mesh);
    }

    public OfferDTO getTile() {
        return tile;
    }

    public void setTile(OfferDTO tile) {
        this.tile = tile;
    }

    private Group createTileMesh(Image front, double width, double height) {
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
        frontMeshView.setMaterial(frontMat);
        frontMeshView.setCullFace(CullFace.BACK);

        return new Group(frontMeshView);
    }
}
