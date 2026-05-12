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
import javafx.geometry.Insets;
import javafx.scene.*;
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
    @FXML AnchorPane anchor;
    @FXML HBox topRow;
    @FXML HBox offerPath;
    @FXML HBox bottomRow;

    @FXML SubScene subScene;
    Group cardsContainer;
    PerspectiveCamera camera;
    @FXML Label debugLabel;

    @FXML TabPane playerTabs;
    @FXML VBox errorToast;
    @FXML VBox ranking;

    private Map<Group, AnimatedCard> meshRegistry;
    private Map<Rectangle, AnimatedCard> refRegistry;
    private Map<Integer, AnimatedCard> idRegistry;

    private List<AnimatedCard> topRowAnim;
    private Rectangle deck;
    private AnimatedObject order;
    private List<AnimatedTile> offerPathAnim;
    private List<AnimatedCard> bottomRowAnim;

    private List<AnimatedTotem> totems;

    private int drawTopCount = 0;
    private int drawBottomCount = 0;

    @FXML
    void initialize() {
        // Initialize structures
        topRowAnim = new ArrayList<>();
        bottomRowAnim = new ArrayList<>();
        offerPathAnim = new ArrayList<>();

        meshRegistry = new HashMap<Group, AnimatedCard>();
        refRegistry = new HashMap<Rectangle, AnimatedCard>();
        idRegistry = new HashMap<Integer, AnimatedCard>();

        GameDTO game = UISession.getGame();
        BoardDTO board = UISession.getGame().getBoard();

        // Create 3D environment
        subScene.widthProperty().bind(anchor.widthProperty());
        subScene.heightProperty().bind(anchor.heightProperty());
        cardsContainer = new Group();
        camera = new PerspectiveCamera(true); // 'true' enables fixed eye position

        subScene.setRoot(cardsContainer);
        subScene.setCamera(camera);

        // Create deck
        deck = new Rectangle(AnimatedCard.cardW, AnimatedCard.cardH);
        deck.setFill(Color.TRANSPARENT);
        offerPath.getChildren().add(deck);
        offerPath.setMargin(deck, new Insets(0, 20, 0, 0));

        // Load order tile
        Image o = new Image(getClass().getResource("/order/" + game.getNumPlayers() + ".png").toExternalForm());
        ImageView iv = new ImageView();
        iv.setImage(o);
        iv.setFitWidth(AnimatedTile.tileW);
        iv.setFitHeight(AnimatedTile.tileH);

        Rectangle ref = new Rectangle(AnimatedTile.tileW, AnimatedTile.tileH);
        ref.setFill(Color.TRANSPARENT);

        order = new AnimatedObject(new Group(iv), ref);
        offerPath.getChildren().add(ref);
        cardsContainer.getChildren().add(order.getMesh());

        // Load offer path
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

        Platform.runLater(() -> {
            cardsContainer.getScene().getRoot().applyCss();
            cardsContainer.getScene().getRoot().layout();
            for (AnimatedObject t : totems) {
                t.resetPosition();
            }
        });

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
            updateInstructionLabel();
            alignCameraToScreenSpace();
            order.resetPosition();

            // React to window resizes
            cardsContainer.getScene().widthProperty().addListener((observable, oldValue, newValue) -> {
                Platform.runLater(this::resetAll);
                alignCameraToScreenSpace();
            });
            cardsContainer.getScene().heightProperty().addListener((observable, oldValue, newValue) -> {
                Platform.runLater(this::resetAll);
                alignCameraToScreenSpace();
            });
            ((Stage) cardsContainer.getScene().getWindow()).maximizedProperty().addListener((observable, oldValue, isMaximized) -> {
                Platform.runLater(this::resetAll);
                alignCameraToScreenSpace();
            });

            ((Stage) cardsContainer.getScene().getWindow()).setOnCloseRequest(event -> {
                try {
                    UISession.getClient().stopGame(UISession.getClient().getNickname());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                System.exit(0);
            });
        });
    }

    private void updateInstructionLabel() {
        GameDTO game = UISession.getGame();
        String instructions = "Its " + UISession.getGame().getPlayerTurn().getName() + "'s turn to ";
        switch (game.getState()) {
            case CHOOSEOFFER:
                instructions += "choose an offer tile";
                break;
            case DRAWCARD:
                instructions += "draw a card";
                break;
            case ENDTURN:
                instructions += "draw a card from the top row";
                break;
            default:
                break;
        }
        debugLabel.setText(instructions);
    }

    private void alignCameraToScreenSpace() {
        // Align the camera to pixels
        double fov = camera.getFieldOfView(); // Default is 30.0
        double halfHeight = subScene.getHeight() / 2.0;
        double distance = halfHeight / Math.tan(Math.toRadians(fov / 2.0));
        camera.setTranslateZ(-distance);
        camera.setTranslateX(subScene.getWidth() / 2.0);
        camera.setTranslateY(subScene.getHeight() / 2.0);

        // Set Clipping Planes
        camera.setNearClip(0.01);
        camera.setFarClip(distance * 2);
    }

    private void resetAll() {
        for (AnimatedCard a : topRowAnim) {
            a.resetPosition();
        }

        for (AnimatedCard a : bottomRowAnim) {
            a.resetPosition();
        }

        order.resetPosition();
        for (AnimatedTile a : offerPathAnim) {
            a.resetPosition();
        }

        for (AnimatedObject t : totems) {
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
    }

    @Override
    public void update(GameDTO game) throws IOException, IllegalActionException {
        // Reset counters
        if (game.getState() != StateDTO.DRAWCARD) {
            drawBottomCount = 0;
            drawTopCount = 0;
        }

        System.out.println(game.getState());

        if (game.getState() == StateDTO.CHOOSEOFFER || game.getState() == StateDTO.ENDTURN) {
            // Find events
            List<CardDTO> events = new ArrayList<>();
            for (CardDTO c : UISession.getGame().getBoard().getBottomRowTribe()) {
                if (c.getType().equals("Event")) {
                    events.add(c);
                }
            }

            // Order them
            // Animate them

            // Continue with the reconciliation
        }

        // Reconcile the playing field with the new GameDTO
        UISession.setGame(game);
        reconcile(game);

        Platform.runLater(() -> {
            // Update toasts
            updateInstructionLabel();
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
            try {
                UISession.getClient().leaveMatch();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("start-game.fxml"));
            Parent startRoot = null;

            try {
                startRoot = fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            VBox errorToastStart = (VBox) startRoot.lookup("#errorToast");
            errorToastStart.setVisible(true);
            ((Label) errorToastStart.getChildren().get(0)).setText("Sorry, the game as been closed due to a disconnection of a player");

            startRoot.lookup("#playAgain").setVisible(true);
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
    }

    /**
     * Reconcile the rendered playing field with the one described by the given GameDTO
     */
    private void reconcile(GameDTO game) {
        Platform.runLater(() -> {
            // Move totems
            for (int i = 0; i < game.getPlayers().size(); i++) {
                AnimatedTotem totem = totems.get(i);
                PlayerDTO p = game.getPlayers().get(i);
                if (game.getPlayers().get(i).getOffer() != '\0') {
                    // When on the offer path
                    AnimatedTile offer = offerPathAnim
                            .stream()
                            .filter(o -> o.getTile().getOrder() == p.getOffer())
                            .findFirst()
                            .get();
                    totem.setReference(offer.getReference());
                    totem.setOffsetOffer();
                    totem.animatePosition(Duration.seconds(0.6));
                } else {
                    // When on the order tile
                    totem.setReference(order.getReference());
                    totem.setOffsetOrder(p.getOrder(), game.getNumPlayers());
                    totem.getMesh().setTranslateZ(-0.01 * (6 - p.getOrder()));
                    totem.getMesh().toFront();
                    totem.animatePosition(Duration.seconds(0.6));
                }
            }

            // Reconcile cards
            List<AnimatedCard> oldCards = new ArrayList<>();
            List<AnimatedCard> newCards = new ArrayList<>();

            // Reconcile top row
            List<AnimatedCard> newTopRowAnim = new ArrayList<>();
            List<AnimatedCard> newBottomRowAnim = new ArrayList<>();
            for (int i = 0; i < game.getBoard().getTopRowTribe().size(); i++) {
                CardDTO c = game.getBoard().getTopRowTribe().get(i);
                AnimatedCard anim = idRegistry.get(c.getId());

                if (anim == null) {
                    // If not, create a new card
                    anim = new AnimatedCard(c, this::cardClicked, deck.localToScene(0, 0).getX(), deck.localToScene(0, 0).getY());
                    meshRegistry.put(anim.getMesh(), anim);
                    refRegistry.put(anim.getReference(), anim);
                    idRegistry.put(anim.getCard().getId(), anim);
                    newCards.add(anim);
                } else {
                    oldCards.add(anim);
                }

                newTopRowAnim.add(anim);
            }
            for (int i = 0; i < game.getBoard().getTopRowBuilding().size(); i++) {
                CardDTO c = game.getBoard().getTopRowBuilding().get(i);
                AnimatedCard anim = idRegistry.get(c.getId());

                if (anim == null) {
                    // If not found, create a new card
                    anim = new AnimatedCard(c, this::cardClicked, deck.localToScene(0, 0).getX(), deck.localToScene(0, 0).getY());
                    meshRegistry.put(anim.getMesh(), anim);
                    refRegistry.put(anim.getReference(), anim);
                    idRegistry.put(anim.getCard().getId(), anim);
                    newCards.add(anim);
                } else {
                    oldCards.add(anim);
                }

                newTopRowAnim.add(anim);
            }

            // Reconcile bottom row
            for (int i = 0; i < game.getBoard().getBottomRowTribe().size(); i++) {
                CardDTO c = game.getBoard().getBottomRowTribe().get(i);
                AnimatedCard anim = idRegistry.get(c.getId());

                if (anim == null) {
                    // If not, create a new card
                    anim = new AnimatedCard(c, this::cardClicked, deck.localToScene(0, 0).getX(), deck.localToScene(0, 0).getY());
                    meshRegistry.put(anim.getMesh(), anim);
                    refRegistry.put(anim.getReference(), anim);
                    idRegistry.put(anim.getCard().getId(), anim);
                    newCards.add(anim);
                } else {
                    oldCards.add(anim);
                }

                newBottomRowAnim.add(anim);
            }
            for (int i = 0; i < game.getBoard().getBottomRowBuilding().size(); i++) {
                CardDTO c = game.getBoard().getBottomRowBuilding().get(i);
                AnimatedCard anim = idRegistry.get(c.getId());

                if (anim == null) {
                    // If not, create a new card
                    anim = new AnimatedCard(c, this::cardClicked, deck.localToScene(0, 0).getX(), deck.localToScene(0, 0).getY());
                    meshRegistry.put(anim.getMesh(), anim);
                    refRegistry.put(anim.getReference(), anim);
                    idRegistry.put(anim.getCard().getId(), anim);
                    newCards.add(anim);
                } else {
                    oldCards.add(anim);
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
                for (AnimatedCard a : oldCards) {
                    a.animatePosition(Duration.seconds(0.5));
                }

                for (int i = 0; i < newCards.size(); i++) {
                    newCards.get(i).animatePosition(Duration.seconds(0.5), Duration.seconds(0.1 * i));
                    newCards.get(i).flip(Duration.seconds(0.1 * (i + 1)));
                }

                for (AnimatedTile a : offerPathAnim) {
                    a.resetPosition();
                }
            });
        });
    }
}
