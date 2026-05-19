package it.polimi.ingsw.UI.GUI;

import it.polimi.ingsw.UI.UISession;
import it.polimi.ingsw.controller.actions.*;
import it.polimi.ingsw.controller.actions.ChooseOfferAction;
import it.polimi.ingsw.controller.actions.ChooseTotemAction;
import it.polimi.ingsw.controller.actions.DrawCardFromBottomAction;
import it.polimi.ingsw.controller.actions.DrawCardFromTopAction;
import it.polimi.ingsw.controller.states.StateDTO;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.BoardDTO;
import it.polimi.ingsw.model.board.OfferDTO;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.networking.UIObserver;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
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

    @FXML VBox totemSelect;
    @FXML HBox totemList;

    @FXML TabPane playerTabs;
    @FXML TilePane players;
    @FXML HBox hand;
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
    private String selectedPlayer;

    @FXML
    void initialize() {
        // Initialize structures
        topRowAnim = new ArrayList<>();
        bottomRowAnim = new ArrayList<>();
        offerPathAnim = new ArrayList<>();

        meshRegistry = new HashMap<>();
        refRegistry = new HashMap<>();
        idRegistry = new HashMap<>();

        selectedPlayer = UISession.getClient().getNickname();

        GameDTO game = UISession.getGame();
        BoardDTO board = UISession.getGame().getBoard();

        // Create 3D environment
        subScene.widthProperty().bind(anchor.widthProperty());
        subScene.heightProperty().bind(anchor.heightProperty());
        cardsContainer = new Group();
        camera = new PerspectiveCamera(true); // 'true' enables fixed eye position
        camera.setDepthTest(DepthTest.ENABLE);

        subScene.setRoot(cardsContainer);
        subScene.setCamera(camera);

        // Create deck
        deck = new Rectangle(AnimatedCard.cardW, AnimatedCard.cardH);
        deck.setFill(Color.TRANSPARENT);
        offerPath.getChildren().add(deck);
        HBox.setMargin(deck, new Insets(0, 20, 0, 0));

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

        for (int i = 0; i < 5; i++) {
            ImageView imgv = new ImageView();
            imgv.setPreserveRatio(true);
            imgv.setImage(new Image(getClass().getResource("/totems/" + i + ".png").toExternalForm()));
            imgv.setFitWidth(50);
            imgv.setId(i + "totemButton");
            imgv.setId(i + "totemButton");
            totemList.getChildren().add(imgv);

            final int j = i;
            imgv.setOnMouseClicked((MouseEvent e) -> {
                try {
                    UISession.getClient().executeAction(new ChooseTotemAction(Totem.values()[j]));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }

        Platform.runLater(() -> {
            cardsContainer.getScene().getRoot().applyCss();
            cardsContainer.getScene().getRoot().layout();
            for (AnimatedObject t : totems) {
                t.resetPosition();
            }
        });

        // Add all cards
        reconcileCards(game);

        // Create player cards
        for (PlayerDTO p : game.getPlayers()) {
            players.getChildren().addAll(GUIBuilder.createPlayerCard(p, this::playerClicked));
        }

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
            case CHOOSETOTEM:
                instructions += "choose a totem";
                break;
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
        camera.setNearClip(distance - 100);
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

        /*if (game.getState() == StateDTO.CHOOSEOFFER || game.getState() == StateDTO.ENDTURN) {
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
        }*/

        // Reconcile the playing field with the new GameDTO
        UISession.setGame(game);
        reconcileTotems(game);
        reconcileCards(game);
        reconcileSelectedHand(game);

        Platform.runLater(() -> {
            // Update toasts
            updateInstructionLabel();
            if (!game.getErrorFlag().isEmpty()) {
                errorToast.setVisible(true);
                ((Label) errorToast.getChildren().getFirst()).setText(game.getErrorFlag());
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

            // Update chosen totems
            game.getPlayers().stream()
                    .map(p -> p.getTotem())
                    .filter(t -> t != null)
                    .forEach(t -> {
                        totemList.getChildren().remove(cardsContainer.getScene().lookup("#" + t.getId() + "totemButton"));
                    });

            for (int i = 0; i < game.getPlayers().size(); i++) {
                PlayerDTO p = game.getPlayers().get(i);
                if (p.getTotem() != null) {
                    totems.get(i).setImgae(new Image(getClass().getResource("/totems/" + p.getTotem().getId() + ".png").toExternalForm()));
                }
            }

            // Hide totem selection
            if (game.getState() != StateDTO.CHOOSETOTEM) {
                totemSelect.setVisible(false);
            }

            // Update player info
            for (PlayerDTO p : game.getPlayers()) {
                ((Text) anchor.getScene().lookup("#" + p.getName() + "Pp")).setText("" + p.getPp());
                ((Text) anchor.getScene().lookup("#" + p.getName() + "Food")).setText("" + p.getFood());
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
            ((Label) errorToastStart.getChildren().getFirst()).setText("Sorry, the game as been closed due to a disconnection of a player");

            startRoot.lookup("#playAgain").setVisible(true);
            startRoot.lookup("#clientSelect").setVisible(false);

            cardsContainer.getScene().setRoot(startRoot);
            UISession.setObserver(fxmlLoader.getController());
        });
    }

    @Override
    public void serverCrashed() {
        System.out.println("Sorry, the server crashed\n");
        System.exit(1);
    }

    @FXML
    void cardClicked(MouseEvent e) {
        GameDTO game = UISession.getGame();
        Group g = (Group) e.getSource();

        // If it's my turn
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
            if (game.getPlayers().stream().noneMatch(p -> p.getOffer() == c.getTile().getOrder())) {
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
        Platform.runLater(() -> {
            cardsContainer.getChildren().remove(c.getMesh());
        });
    }

    /**
     * Reconcile the rendered totem positions with the ones described by the given GameDTO
     */
    private void reconcileTotems(GameDTO game) {
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
                    ((MeshView) totem.getMesh().getChildren().getFirst()).setTranslateZ(-(1 + p.getOrder()));
                    totem.animatePosition(Duration.seconds(0.6));
                }
            }
        });
    }

    /**
     * Reconcile the rendered playing field with the one described by the given GameDTO
     */
    private void reconcileCards(GameDTO game) {
        Platform.runLater(() -> {
            // Reconcile cards
            List<AnimatedCard> oldCards = new ArrayList<>();
            List<AnimatedCard> newCards = new ArrayList<>();

            // Reconcile top row
            List<AnimatedCard> newTopRowAnim = new ArrayList<>();
            List<AnimatedCard> newBottomRowAnim = new ArrayList<>();

            List<List<AnimatedCard>> animRef = new ArrayList<List<AnimatedCard>>(Arrays.asList(
                    newTopRowAnim, newTopRowAnim,
                    newBottomRowAnim, newBottomRowAnim
            ));
            List<List<? extends CardDTO>> dtoRef = new ArrayList<List<? extends CardDTO>>(Arrays.asList(
                    game.getBoard().getTopRowTribe(), game.getBoard().getTopRowBuilding(),
                    game.getBoard().getBottomRowTribe(), game.getBoard().getBottomRowBuilding()
            ));

            for (int j = 0; j < animRef.size(); j++) {
                List<AnimatedCard> anims = animRef.get(j);
                List<? extends CardDTO> cards = dtoRef.get(j);
                for (int i = 0; i < cards.size(); i++) {
                    CardDTO c = cards.get(i);
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

                    anims.add(anim);
                }
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

                order.resetPosition();
                for (AnimatedTile a : offerPathAnim) {
                    a.resetPosition();
                }
            });
        });
    }

    /**
     * Reconcile the rendered hand of the selected player with the one described by the given GameDTO
     */
    private void reconcileSelectedHand(GameDTO game) {
        Platform.runLater(() -> {
            for (PlayerDTO p : game.getPlayers()) {
                if (p.getName().equals(selectedPlayer)) {
                    List<CardDTO> newHand = new ArrayList<>(p.getArtists());
                    newHand.addAll(p.getBuildings());
                    newHand.addAll(p.getBuilders());
                    newHand.addAll(p.getGatherers());
                    newHand.addAll(p.getHunters());
                    newHand.addAll(p.getInventors());
                    newHand.addAll(p.getShamans());

                    for (CardDTO c : newHand) {
                        // If absent, insert it
                        if (hand.getChildren().stream().noneMatch(m -> meshRegistry.get(m).getCard().getId() == c.getId())){
                            hand.getChildren().add(idRegistry.get(c.getId()).getMesh());
                        }
                    }
                }
            }
        });
    }

    @FXML
    void playerClicked(MouseEvent e) {
        String player = ((Label) ((VBox) e.getSource()).getChildren().get(0)).getText();
        selectedPlayer = player;

        // Empty the hand
        hand.getChildren().clear();

        // Refill the hand
        reconcileSelectedHand(UISession.getGame());
    }
}
