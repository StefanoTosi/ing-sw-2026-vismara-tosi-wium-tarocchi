package it.polimi.ingsw.UI.GUI;

import it.polimi.ingsw.UI.UISession;
import it.polimi.ingsw.controller.actions.ChooseOfferAction;
import it.polimi.ingsw.controller.actions.DrawCardFromBottomAction;
import it.polimi.ingsw.controller.actions.DrawCardFromTopAction;
import it.polimi.ingsw.controller.states.StateDTO;
import it.polimi.ingsw.model.CardDTO;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.PlayerDTO;
import it.polimi.ingsw.model.board.BoardDTO;
import it.polimi.ingsw.model.board.OfferDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.UIObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FieldController implements UIObserver {
    @FXML HBox topRow;
    @FXML HBox offerPath;
    @FXML HBox bottomRow;

    @FXML Pane cardsContainer;
    @FXML Label debugLabel;

    @FXML TabPane playerTabs;

    private List<AnimatedCard> topRowAnim;
    private List<AnimatedTile> offerPathAnim;
    private List<AnimatedCard> bottomRowAnim;

    private int drawTopCount = 0;
    private int drawBottomCount = 0;

    @FXML
    void initialize() {
        topRowAnim = new ArrayList<>();
        bottomRowAnim = new ArrayList<>();
        offerPathAnim = new ArrayList<>();

        GameDTO game = UISession.getGame();
        BoardDTO board = UISession.getGame().getBoard();

        // Load top row
        for (int i = 0; i < board.getTopRowTribe().size(); i++) {
            AnimatedCard a = new AnimatedCard(board.getTopRowTribe().get(i), this::cardClicked);
            topRowAnim.add(a);
            topRow.getChildren().add(a.getReference());
            cardsContainer.getChildren().add(a.getMesh());
        }

        for (int i = 0; i < board.getTopRowBuilding().size(); i++) {
            AnimatedCard a = new AnimatedCard(board.getTopRowBuilding().get(i), this::cardClicked);
            topRowAnim.add(a);
            topRow.getChildren().add(a.getReference());
            cardsContainer.getChildren().add(a.getMesh());
        }

        // Load bottom row
        for (int i = 0; i < board.getBottomRowTribe().size(); i++) {
            AnimatedCard a = new AnimatedCard(board.getBottomRowTribe().get(i), this::cardClicked);
            bottomRowAnim.add(a);
            bottomRow.getChildren().add(a.getReference());
            cardsContainer.getChildren().add(a.getMesh());
        }

        for (int i = 0; i < board.getBottomRowBuilding().size(); i++) {
            AnimatedCard a = new AnimatedCard(board.getBottomRowBuilding().get(i), this::cardClicked);
            bottomRowAnim.add(a);
            bottomRow.getChildren().add(a.getReference());
            cardsContainer.getChildren().add(a.getMesh());
        }

        // Load offer path
        for (int i = 0; i < board.getOfferPath().size(); i++) {
            offerPathAnim.add(new AnimatedTile(board.getOfferPath().get(i), offerPath, cardsContainer, this::tileClicked));
        }

        // Create player tabs
        playerTabs.getTabs().clear();
        for (PlayerDTO p : game.getPlayers()) {
            playerTabs.getTabs().add(GUIBuilder.createPlayerTab(p));
        }
        String myTab = UISession.getClient().getNickname() + "Tab";
        playerTabs.getTabs().stream()
                .filter(tab -> myTab.equals(tab.getId()))
                .findFirst()
                .ifPresent(tab -> playerTabs.getSelectionModel().select(tab));

        // TODO: Render totems

        // Place animated cards in the cards container
        Platform.runLater(() -> {
            debugLabel.setText("Its the turn of " + UISession.getGame().getPlayerTurn().getName());
            animateAll(Duration.seconds(1));

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

    private void animateAll(Duration d) {
        for (AnimatedCard a : topRowAnim) {
            a.animatePosition(d);
        }

        for (AnimatedCard a : bottomRowAnim) {
            a.animatePosition(d);
        }

        for (AnimatedTile a : offerPathAnim) {
            a.resetPosition();
        }

    }

    @Override
    public void update(GameDTO game) throws IOException, IllegalActionException {
        UISession.setGame(game);

        // Reset counters
        if (game.getState() != StateDTO.DRAWCARD) {
            drawBottomCount = 0;
            drawTopCount = 0;
        }

        Platform.runLater(() -> {
            debugLabel.setText("Its the turn of " + game.getPlayerTurn().getName() + " : " + game.getState());

            // Update player info
            for (PlayerDTO p : game.getPlayers()) {
                ((Label) playerTabs.getScene().lookup("#" + p.getName() + "Pp")).setText("Prestige points: " + p.getPp());
                ((Label) playerTabs.getScene().lookup("#" + p.getName() + "Food")).setText("Food: " + p.getFood());
            }

            // Reconcile top row
            List<AnimatedCard> newTopRowAnim = new ArrayList<>();
            List<AnimatedCard> newBottomRowAnim = new ArrayList<>();
            for (int i = 0; i < game.getBoard().getTopRowTribe().size(); i++) {
                CardDTO c = game.getBoard().getTopRowTribe().get(i);
                AnimatedCard anim;
                Optional<AnimatedCard> find = topRowAnim.stream()
                        .filter(a -> a.getCard().getId() == c.getId()).findFirst();
                if (find.isPresent()) {
                    // If we can find the card somewhere
                    anim = find.get();
                } else {
                    // If not, create a new card
                    anim = new AnimatedCard(c, this::cardClicked);
                }

                newTopRowAnim.add(anim);
            }
            for (int i = 0; i < game.getBoard().getTopRowBuilding().size(); i++) {
                CardDTO c = game.getBoard().getTopRowBuilding().get(i);
                AnimatedCard anim;
                Optional<AnimatedCard> find = topRowAnim.stream()
                        .filter(a -> a.getCard().getId() == c.getId()).findFirst();
                if (find.isPresent()) {
                    // If we can find the card somewhere
                    anim = find.get();
                } else {
                    // If not, create a new card
                    anim = new AnimatedCard(c, this::cardClicked);
                }

                newTopRowAnim.add(anim);
            }

            // Reconcile bottom row
            for (int i = 0; i < game.getBoard().getBottomRowTribe().size(); i++) {
                CardDTO c = game.getBoard().getBottomRowTribe().get(i);
                AnimatedCard anim;
                Optional<AnimatedCard> find = Stream.concat(topRowAnim.stream(), bottomRowAnim.stream())
                        .filter(a -> a.getCard().getId() == c.getId()).findFirst();
                if (find.isPresent()) {
                    // If we can find the card somewhere
                    anim = find.get();
                } else {
                    // If not, create a new card
                    anim = new AnimatedCard(c, this::cardClicked);
                }

                newBottomRowAnim.add(anim);
            }
            for (int i = 0; i < game.getBoard().getBottomRowBuilding().size(); i++) {
                CardDTO c = game.getBoard().getBottomRowBuilding().get(i);
                AnimatedCard anim;
                Optional<AnimatedCard> find = Stream.concat(topRowAnim.stream(), bottomRowAnim.stream())
                        .filter(a -> a.getCard().getId() == c.getId()).findFirst();
                if (find.isPresent()) {
                    // If we can find the card somewhere
                    anim = find.get();
                } else {
                    // If not, create a new card
                    anim = new AnimatedCard(c, this::cardClicked);
                }

                newBottomRowAnim.add(anim);
            }

            // Remove all old card
            topRow.getChildren().clear();
            cardsContainer.getChildren().removeAll(topRowAnim.stream().map(a -> a.getMesh()).toList());
            bottomRow.getChildren().clear();
            cardsContainer.getChildren().removeAll(bottomRowAnim.stream().map(a -> a.getMesh()).toList());

            // Add all new cards
            topRow.getChildren().addAll(newTopRowAnim.stream().map(a -> a.getReference()).toList());
            cardsContainer.getChildren().addAll(newTopRowAnim.stream().map(a -> a.getMesh()).toList());
            topRowAnim = newTopRowAnim;
            bottomRow.getChildren().addAll(newBottomRowAnim.stream().map(a -> a.getReference()).toList());
            cardsContainer.getChildren().addAll(newBottomRowAnim.stream().map(a -> a.getMesh()).toList());
            bottomRowAnim = newBottomRowAnim;

            cardsContainer.getScene().getRoot().applyCss();
            cardsContainer.getScene().getRoot().layout();

            Platform.runLater(()-> {
                animateAll(Duration.seconds(0.2));
            });
        });
    }

    @Override
    public void closingGame(GameDTO game) throws IOException, IllegalActionException, ClassNotFoundException, InterruptedException {

    }

    @FXML
    void cardClicked(MouseEvent e) {
        GameDTO game = UISession.getGame();

        // If its DrawCardState and my turn
        if (game.getState() == StateDTO.DRAWCARD && game.getPlayerTurn().getName().equals(UISession.getClient().getNickname())) {
            OfferDTO offer = game.getBoard().getOfferPath().stream()
                    .filter(o -> o.getOrder() == game.getPlayerTurn().getOffer())
                    .findFirst().get();

            Platform.runLater(() -> {
                // Find the card
                Group g = (Group) e.getSource();
                AnimatedCard c = topRowAnim.stream()
                        .filter(a -> a.getMesh() == g)
                        .findFirst().orElse(null);

                if (c != null && drawTopCount < offer.getDrawTop()) {
                    // Is in top row
                    int i = topRowAnim.indexOf(c);
                    topRowAnim.remove(c);
                    topRow.getChildren().remove(c.getReference());

                    // Send action
                    try {
                        UISession.getClient().executeAction(new DrawCardFromTopAction(i));
                        drawTopCount += 1;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (drawBottomCount < offer.getDrawBottom()) {
                    // Is in bottom row
                    c = bottomRowAnim.stream()
                            .filter(a -> a.getMesh() == g)
                            .findFirst().orElse(null);
                    int i = bottomRowAnim.indexOf(c);
                    bottomRowAnim.remove(c);
                    bottomRow.getChildren().remove(c.getReference());

                    // Send action
                    try {
                        UISession.getClient().executeAction(new DrawCardFromBottomAction(i));
                        drawBottomCount += 1;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                // Animate the cards
                cardsContainer.getScene().getRoot().applyCss();
                cardsContainer.getScene().getRoot().layout();

                c.animatePosition(Duration.seconds(0.2), -c.cardW, -c.cardH);
                c.spin();
                // animateAll(Duration.seconds(0.2));
            });
        }
    }

    @FXML
    void tileClicked(MouseEvent e) {
        GameDTO game = UISession.getGame();

        // If its ChooseOfferState and my turn
        if (game.getState() == StateDTO.CHOOSEOFFER && game.getPlayerTurn().getName().equals(UISession.getClient().getNickname())) {
            // Find the clicked tile
            Group g = (Group) e.getSource();
            AnimatedTile c = offerPathAnim.stream()
                    .filter(a -> a.getMesh() == g)
                    .findFirst().get();

            // Choose it
            try {
                UISession.getClient().executeAction(new ChooseOfferAction(c.getTile().getOrder()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // Otherwise do nothing
    }
}
