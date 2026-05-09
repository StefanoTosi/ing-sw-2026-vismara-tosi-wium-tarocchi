package it.polimi.ingsw.UI.GUI;

import it.polimi.ingsw.UI.UISession;
import it.polimi.ingsw.controller.actions.ChooseOfferAction;
import it.polimi.ingsw.controller.actions.DrawCardFromBottomAction;
import it.polimi.ingsw.controller.actions.DrawCardFromTopAction;
import it.polimi.ingsw.controller.states.StateDTO;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.BoardDTO;
import it.polimi.ingsw.model.board.OfferDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.UIObserver;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.css.Rect;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class FieldController implements UIObserver {
    @FXML HBox topRow;
    @FXML HBox offerPath;
    @FXML HBox bottomRow;

    @FXML Pane cardsContainer;
    @FXML Label debugLabel;

    @FXML TabPane playerTabs;
    @FXML VBox errorToast;
    @FXML VBox ranking;

    private Map<Group, AnimatedCard> meshRegistry;
    private Map<Rectangle, AnimatedCard> refRegistry;
    private Map<Integer, AnimatedCard> idRegistry;

    private List<AnimatedCard> topRowAnim;
    private List<AnimatedTile> offerPathAnim;
    private List<AnimatedCard> bottomRowAnim;

    private List<AnimatedTotem> totems;

    private int drawTopCount = 0;
    private int drawBottomCount = 0;

    @FXML
    void initialize() {
        topRowAnim = new ArrayList<>();
        bottomRowAnim = new ArrayList<>();
        offerPathAnim = new ArrayList<>();

        meshRegistry = new HashMap<Group, AnimatedCard>();
        refRegistry = new HashMap<Rectangle, AnimatedCard>();
        idRegistry = new HashMap<Integer, AnimatedCard>();

        GameDTO game = UISession.getGame();
        BoardDTO board = UISession.getGame().getBoard();

        // Load offer path
        Image o = new Image(getClass().getResource("/order/" + game.getNumPlayers() + ".png").toExternalForm());
        ImageView order = new ImageView();
        order.setImage(o);
        order.setFitWidth(AnimatedTile.tileW);
        order.setFitHeight(AnimatedTile.tileH);
        offerPath.getChildren().add(order);

        for (int i = 0; i < board.getOfferPath().size(); i++) {
            offerPathAnim.add(new AnimatedTile(board.getOfferPath().get(i), offerPath, cardsContainer, this::tileClicked));
        }

        // Load totems
        totems = new ArrayList<>();
        for (int i = 0; i < game.getPlayers().size(); i++) {
            totems.add(new AnimatedTotem(i));
            cardsContainer.getChildren().add(totems.get(i).getMesh());
            cardsContainer.getChildren().add(totems.get(i).getReference());
            totems.get(i).resetPosition();
        }

        // Add all cards
        reconcile(game);

        // Style player tabs
        Platform.runLater(() -> {
            Node headerBackground = playerTabs.lookup(".tab-header-background");
            if (headerBackground != null) {
                headerBackground.setStyle("-fx-background-color: transparent;");
            }
        });

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
        playerTabs.getStylesheets().add("data:text/css," +
                ".tab-pane .tab:selected {" +
                "    -fx-border-color: #F04D3B !important;" + // Change this to your color
                "}");

        // Place animated cards in the cards container
        Platform.runLater(() -> {
            debugLabel.setText("Its the turn of " + UISession.getGame().getPlayerTurn().getName());
            for (AnimatedObject t : totems) {
                t.resetPosition();
            }

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

            ((Stage) cardsContainer.getScene().getWindow()).setOnCloseRequest(event -> {
                System.exit(0);
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

        for (AnimatedObject t : totems) {
            t.getMesh().toFront();
            t.resetPosition();
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

        for (AnimatedObject t : totems) {
            t.getMesh().toFront();
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

        System.out.println(game.getState());

        // Move totems
        for (int i = 0; i < game.getPlayers().size(); i++) {
            if (game.getPlayers().get(i).getOffer() != '\0') {
                AnimatedObject totem = totems.get(i);
                PlayerDTO p = game.getPlayers().get(i);
                AnimatedTile offer = offerPathAnim
                        .stream()
                        .filter(o -> o.getTile().getOrder() == p.getOffer())
                        .findFirst()
                        .get();
                totem.getReference().setLayoutX(offer.getReference().localToScene(0, 0).getX());
                totem.getReference().setLayoutY(offer.getReference().localToScene(0, 0).getY());
                totem.animatePosition(Duration.seconds(0.6));
            } else {
                AnimatedObject totem = totems.get(i);
                totem.getReference().setLayoutX(offerPathAnim.get(0).getReference().localToScene(0, 0).getX() - AnimatedTile.tileW);
                totem.getReference().setLayoutY(offerPathAnim.get(0).getReference().localToScene(0, 0).getY());
                totem.animatePosition(Duration.seconds(0.6));
            }
        }

        // Reconcile the playing field with the new GameDTO
        reconcile(game);

        Platform.runLater(() -> {
            // Update toasts
            debugLabel.setText("Its the turn of " + game.getPlayerTurn().getName() + " : " + game.getState());
            if (!game.getErrorFlag().equals("")) {
                errorToast.setVisible(true);
                ((Label) errorToast.getChildren().get(0)).setText(game.getErrorFlag());
            } else {
                errorToast.setVisible(false);
            }

            // Update ranking
            if (game.getState() == StateDTO.ENDGAME) {
                ranking.setVisible(true);
                for (PlayerDTO p : game.getRankings()) {
                    Label l = new Label(p.getName());
                    l.setTextFill(Color.valueOf("#fff"));
                    ranking.getChildren().add(l);
                }
            }

            // Update player info
            for (PlayerDTO p : game.getPlayers()) {
                ((Label) playerTabs.getScene().lookup("#" + p.getName() + "Pp")).setText("Prestige points: " + p.getPp());
                ((Label) playerTabs.getScene().lookup("#" + p.getName() + "Food")).setText("Food: " + p.getFood());
            }
        });
    }

    @Override
    public void closingGame(GameDTO game) throws IOException, IllegalActionException, ClassNotFoundException, InterruptedException {
        Platform.runLater(() -> {
            System.out.println("ok1");
            try {
                UISession.getClient().leaveMatch();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("ok2");
            FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("start-game.fxml"));
            Parent startRoot = null;

            try {
                startRoot = fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("ok3");
            VBox errorToastStart = (VBox) startRoot.lookup("#errorToast");
            errorToastStart.setVisible(true);
            ((Label) errorToastStart.getChildren().get(0)).setText("Sorry, the game as been closed due to a disconnection of a player");

            startRoot.lookup("#login").setVisible(true);
            startRoot.lookup("#clientSelect").setVisible(false);

            cardsContainer.getScene().setRoot(startRoot);
            UISession.setObserver(fxmlLoader.getController());
        });
    }

    @Override
    public void serverCrashed() throws IOException, IllegalActionException, InterruptedException{
        System.out.println("Sorry, the server crashed\n");
        System.exit(1);
    }

    @FXML
    void cardClicked(MouseEvent e) {
        GameDTO game = UISession.getGame();
        Group g = (Group) e.getSource();

        // If its my turn
        if (game.getPlayerTurn().getName().equals(UISession.getClient().getNickname())) {
            if (game.getState() == StateDTO.DRAWCARD) {
                OfferDTO offer = game.getBoard().getOfferPath().stream()
                        .filter(o -> o.getOrder() == game.getPlayerTurn().getOffer())
                        .findFirst().get();

                Platform.runLater(() -> {
                    // Find the card
                    AnimatedCard c = topRowAnim.stream()
                            .filter(a -> a.getMesh() == g)
                            .findFirst().orElse(null);

                    if (c != null && drawTopCount < offer.getDrawTop()) {
                        if (!c.getCard().getType().equals("Event")) {
                            // Is in top row
                            int i = topRowAnim.indexOf(c);
                            topRowAnim.remove(c);
                            topRow.getChildren().remove(c.getReference());

                            // Send action
                            try {
                                UISession.getClient().executeAction(new DrawCardFromTopAction(i));
                                drawCard(c);
                                drawTopCount += 1;
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            meshRegistry.get(g).shake();
                        }
                    } else if (drawBottomCount < offer.getDrawBottom()) {
                        // Is in bottom row
                        c = bottomRowAnim.stream()
                                .filter(a -> a.getMesh() == g)
                                .findFirst().orElse(null);
                        if (!c.getCard().getType().equals("Event")) {
                            int i = bottomRowAnim.indexOf(c);
                            bottomRowAnim.remove(c);
                            bottomRow.getChildren().remove(c.getReference());

                            // Send action
                            try {
                                UISession.getClient().executeAction(new DrawCardFromBottomAction(i));
                                drawCard(c);
                                drawBottomCount += 1;
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            meshRegistry.get(g).shake();
                        }
                    }

                });
            } else if (game.getState() == StateDTO.ENDTURN) {
                Platform.runLater(() -> {
                    // Find the card
                    AnimatedCard c = topRowAnim.stream()
                            .filter(a -> a.getMesh() == g)
                            .findFirst().orElse(null);

                    if (c != null && !c.getCard().getType().equals("Event")) {
                        // Is in top row
                        int i = topRowAnim.indexOf(c);
                        topRowAnim.remove(c);
                        topRow.getChildren().remove(c.getReference());

                        // Send action
                        try {
                            UISession.getClient().executeAction(new DrawCardFromTopAction(i));
                            drawCard(c);
                            drawTopCount += 1;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        meshRegistry.get(g).shake();
                    }
                });
            }
        } else {
            Platform.runLater(() -> {
                meshRegistry.get(g).shake();
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

            // Check its free
            if (!game.getPlayers().stream().anyMatch(p -> p.getOffer() == c.getTile().getOrder())) {
                // Choose it
                try {
                    UISession.getClient().executeAction(new ChooseOfferAction(c.getTile().getOrder()));

                    Platform.runLater(() -> {
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
        // Otherwise do nothing
    }


    /**
     * Move the given AnimatedCard to the hand of the current player
     */
    private void drawCard(AnimatedCard c) {
        // Put card in the tab
        HBox hand = (HBox) cardsContainer.getScene().lookup("#" + UISession.getClient().getNickname() + "Hand");
        cardsContainer.getChildren().remove(c.getMesh());
        hand.getChildren().add(c.getMesh());

        // Animate the cards
        cardsContainer.getScene().getRoot().applyCss();
        cardsContainer.getScene().getRoot().layout();

        // c.animatePosition(Duration.seconds(0.5));
        // c.spin();
        // c.getMesh().toFront();
    }

    /**
     * Reconcile the rendered playing field with the one described by the given GameDTO
     */
    private void reconcile(GameDTO game) {
        Platform.runLater(() -> {
            // Reconcile top row
            List<AnimatedCard> newTopRowAnim = new ArrayList<>();
            List<AnimatedCard> newBottomRowAnim = new ArrayList<>();
            for (int i = 0; i < game.getBoard().getTopRowTribe().size(); i++) {
                CardDTO c = game.getBoard().getTopRowTribe().get(i);
                AnimatedCard anim = idRegistry.get(c.getId());

                if (anim == null) {
                    // If not, create a new card
                    anim = new AnimatedCard(c, this::cardClicked);
                    meshRegistry.put(anim.getMesh(), anim);
                    refRegistry.put(anim.getReference(), anim);
                    idRegistry.put(anim.getCard().getId(), anim);
                }

                newTopRowAnim.add(anim);
            }
            for (int i = 0; i < game.getBoard().getTopRowBuilding().size(); i++) {
                CardDTO c = game.getBoard().getTopRowBuilding().get(i);
                AnimatedCard anim = idRegistry.get(c.getId());

                if (anim == null) {
                    // If not found, create a new card
                    anim = new AnimatedCard(c, this::cardClicked);
                    meshRegistry.put(anim.getMesh(), anim);
                    refRegistry.put(anim.getReference(), anim);
                    idRegistry.put(anim.getCard().getId(), anim);
                }

                newTopRowAnim.add(anim);
            }

            // Reconcile bottom row
            for (int i = 0; i < game.getBoard().getBottomRowTribe().size(); i++) {
                CardDTO c = game.getBoard().getBottomRowTribe().get(i);
                AnimatedCard anim = idRegistry.get(c.getId());

                if (anim == null) {
                    // If not, create a new card
                    anim = new AnimatedCard(c, this::cardClicked);
                    meshRegistry.put(anim.getMesh(), anim);
                    refRegistry.put(anim.getReference(), anim);
                    idRegistry.put(anim.getCard().getId(), anim);
                }

                newBottomRowAnim.add(anim);
            }
            for (int i = 0; i < game.getBoard().getBottomRowBuilding().size(); i++) {
                CardDTO c = game.getBoard().getBottomRowBuilding().get(i);
                AnimatedCard anim = idRegistry.get(c.getId());

                if (anim == null) {
                    // If not, create a new card
                    anim = new AnimatedCard(c, this::cardClicked);
                    meshRegistry.put(anim.getMesh(), anim);
                    refRegistry.put(anim.getReference(), anim);
                    idRegistry.put(anim.getCard().getId(), anim);
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

            // Reconcile player hands
            for (PlayerDTO p : game.getPlayers()) {
                HBox handVBox = (HBox) cardsContainer.getScene().lookup("#" + p.getName() + "Hand");
                List<CardDTO> hand = new ArrayList<>(p.getArtists());
                hand.addAll(p.getBuildings());
                hand.addAll(p.getBuilders());
                hand.addAll(p.getGatherers());
                hand.addAll(p.getHunters());
                hand.addAll(p.getInventors());
                hand.addAll(p.getShamans());

                for (CardDTO c : hand) {
                    // If absent, insert it
                    if (!handVBox.getChildren().stream().anyMatch(m -> meshRegistry.get(m).getCard().getId() == c.getId())){
                        handVBox.getChildren().add(idRegistry.get(c.getId()).getMesh());
                    }
                }
            }

            // Refresh layout
            cardsContainer.getScene().getRoot().applyCss();
            cardsContainer.getScene().getRoot().layout();

            Platform.runLater(()-> {
                animateAll(Duration.seconds(0.5));
            });
        });
    }
}
