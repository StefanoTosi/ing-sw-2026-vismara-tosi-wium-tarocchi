package it.polimi.ingsw.networking.GUI;

import it.polimi.ingsw.controller.actions.ChooseOfferAction;
import it.polimi.ingsw.controller.states.StateDTO;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.board.BoardDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.UIObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FieldController implements UIObserver {
    @FXML HBox topRow;
    @FXML HBox offerPath;
    @FXML HBox bottomRow;

    @FXML Pane cardsContainer;
    @FXML Label debugLabel;

    private List<AnimatedCard> topRowAnim;
    private List<AnimatedTile> offerPathAnim;
    private List<AnimatedCard> bottomRowAnim;

    @FXML
    void initialize() {
        topRowAnim = new ArrayList<>();
        bottomRowAnim = new ArrayList<>();
        offerPathAnim = new ArrayList<>();

        BoardDTO board = GUISession.getGame().getBoard();

        // Load top row
        for (int i = 0; i < board.getTopRowTribe().size(); i++) {
            topRowAnim.add(new AnimatedCard(board.getTopRowTribe().get(i), topRow, cardsContainer, this::cardClicked));
        }

        for (int i = 0; i < board.getTopRowBuilding().size(); i++) {
            topRowAnim.add(new AnimatedCard(board.getTopRowBuilding().get(i), topRow, cardsContainer, this::cardClicked));
        }

        // Load bottom row
        for (int i = 0; i < board.getBottomRowTribe().size(); i++) {
            topRowAnim.add(new AnimatedCard(board.getBottomRowTribe().get(i), bottomRow, cardsContainer, this::cardClicked));
        }

        for (int i = 0; i < board.getBottomRowBuilding().size(); i++) {
            topRowAnim.add(new AnimatedCard(board.getBottomRowBuilding().get(i), topRow, cardsContainer, this::cardClicked));
        }

        // Load offer path
        for (int i = 0; i < board.getOfferPath().size(); i++) {
            offerPathAnim.add(new AnimatedTile(board.getOfferPath().get(i), offerPath, cardsContainer, this::tileClicked));
        }

        // TODO: Render totems

        // Place animated cards in the cards container
        Platform.runLater(() -> {
            debugLabel.setText("Its the turn of " + GUISession.getGame().getPlayerTurn().getName());
            animateAll();

            // React to window resizes
            cardsContainer.getScene().widthProperty().addListener((observable, oldValue, newValue) -> {
                Platform.runLater(this::resetAll);
            });
            cardsContainer.getScene().heightProperty().addListener((observable, oldValue, newValue) -> {
                Platform.runLater(this::resetAll);
            });
            ((Stage) cardsContainer.getScene().getWindow()).maximizedProperty().addListener((observable, oldValue, isMaximized) -> {
                Platform.runLater(this::resetAll);
            });
        });

    }

    private void resetAll() {
        for (AnimatedCard a : topRowAnim) {
            a.resetPosition();
        }

        for (AnimatedCard a : bottomRowAnim) {
            a.resetPosition();
        }

        for (AnimatedTile a : offerPathAnim) {
            a.resetPosition();
        }
    }

    private void animateAll() {
        for (AnimatedCard a : topRowAnim) {
            a.animatePosition();
        }

        for (AnimatedCard a : bottomRowAnim) {
            a.animatePosition();
        }

        for (AnimatedTile a : offerPathAnim) {
            a.resetPosition();
        }

    }

    @Override
    public void update(GameDTO game) throws IOException, IllegalActionException {
        GUISession.setGame(game);

        Platform.runLater(() -> {
            debugLabel.setText("Its the turn of " + game.getPlayerTurn().getName());
        });
    }

    @Override
    public void closingGame(GameDTO game) throws IOException, IllegalActionException, ClassNotFoundException, InterruptedException {

    }

    @FXML
    void cardClicked(MouseEvent e) {
        System.out.println("Clicked");
        Group g = (Group) e.getSource();
        AnimatedCard c = Stream.concat(topRowAnim.stream(), bottomRowAnim.stream())
                .filter(a -> a.getMesh() == g)
                .findFirst().get();
        System.out.println(c.getCard().getName());
        c.spin();
    }

    @FXML
    void tileClicked(MouseEvent e) {
        GameDTO game = GUISession.getGame();
        // If its ChooseOfferState and my turn
        if (game.getState() == StateDTO.CHOOSEOFFER && game.getPlayerTurn().getName().equals(GUISession.getClient().getNickname())) {
            // Find the clicked tile
            Group g = (Group) e.getSource();
            AnimatedTile c = offerPathAnim.stream()
                    .filter(a -> a.getMesh() == g)
                    .findFirst().get();

            // Choose it
            try {
                GUISession.getClient().executeAction(new ChooseOfferAction(c.getTile().getOrder()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // Otherwise do nothing
    }
}
