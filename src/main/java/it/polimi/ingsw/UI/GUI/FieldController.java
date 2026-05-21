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
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Mesh;
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
    @FXML Text round;

    private Map<Group, AnimatedCard> meshRegistry;
    private Map<Rectangle, AnimatedCard> refRegistry;
    private Map<Integer, AnimatedCard> idRegistry;

    private List<AnimatedCard> topRowAnim;
    private Rectangle deck;
    private AnimatedCard topDeck;
    private AnimatedObject order;
    private List<AnimatedTile> offerPathAnim;
    private List<AnimatedCard> bottomRowAnim;

    private List<AnimatedTotem> totems;

    private String selectedPlayer;

    /**
     * Initializes the playing field, by initializing all structures and GUI elements needed for the game,
     * such as totems, player information cards and offer and order tiles
     */
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

        // Create player info cards
        for (PlayerDTO p : game.getPlayers()) {
            players.getChildren().addAll(GUIBuilder.createPlayerCard(p, this::playerClicked));
        }

        // Update the layout
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

            // Send a close signal to the server when the player disconnects
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

    /**
     * Updates the label at the top of the screen with information regarding whose turn it is and what
     * action is required
     */
    private void updateInstructionLabel() {
        Platform.runLater(() -> {
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
        });
    }

    /**
     * Updates the error toast that appears at the bottom of the screen when an IllegalActionException
     * happened on the server
     */
    private void updateErrorToast() {
        GameDTO game = UISession.getGame();
        Platform.runLater(() -> {
            if (!game.getErrorFlag().isEmpty()) {
                errorToast.setVisible(true);
                ((Label) errorToast.getChildren().getFirst()).setText(game.getErrorFlag());
            } else {
                errorToast.setVisible(false);
            }
        });
    }

    /**
     * Updates the final ranking of the game
     */
    private void updateRanking() {
        GameDTO game = UISession.getGame();
        Platform.runLater(() -> {
            // Update ranking
            if (game.getState() == StateDTO.ENDGAME) {
                ranking.setVisible(true);
                for (PlayerDTO p : game.getRankings()) {
                    Label l = new Label(p.getName());
                    l.setTextFill(Color.valueOf("#fff"));
                    ranking.getChildren().add(l);
                }
            }
        });
    }

    /**
     * Updates the food and PP information for all players
     */
    private void updatePlayerInfo() {
        GameDTO game = UISession.getGame();
        Platform.runLater(() -> {
            // Update player info
            for (PlayerDTO p : game.getPlayers()) {
                ((Text) anchor.getScene().lookup("#" + p.getName() + "Pp")).setText("" + p.getPp());
                ((Text) anchor.getScene().lookup("#" + p.getName() + "Food")).setText("" + p.getFood());
            }
        });
    }

    /**
     * Corrects the 3D camera's position to be at the center of the playing field and far enough for
     * the whole field to fit in the screen
     */
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

    /**
     * Instantly resets the position of all animated objects in the window
     */
    private void resetAll() {
        for (AnimatedCard a : topRowAnim) {
            a.resetPosition();
        }

        for (AnimatedCard a : bottomRowAnim) {
            a.resetPosition();
        }

        order.resetPosition();
        topDeck.resetPosition();
        for (AnimatedTile a : offerPathAnim) {
            a.resetPosition();
        }

        for (AnimatedObject t : totems) {
            t.resetPosition();
        }
    }

    /**
     * Handles server notification, by reconciling how the playing field and UI are rendered in accordance
     * With the new GameDTO that it received
     *
     * @param game the GameDTO sent by the server as a notification
     */
    @Override
    public void update(GameDTO game) throws IOException, IllegalActionException {
        System.out.println(game.getState());

        // Reconcile the playing field with the new GameDTO
        UISession.setGame(game);
        reconcileTotems(game);
        reconcileCards(game);
        reconcileSelectedHand(game);

        // Update toasts
        updateInstructionLabel();
        updateErrorToast();

        // Update misc
        updateRanking();
        Platform.runLater(() -> {
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

            // Round
            round.setText("Round " + game.getTurnNumber());
        });
    }

    /**
     * Handles the closing signal form the server, when a player has disconnected
     */
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

    /**
     * Handles when the server crashes
     */
    @Override
    public void serverCrashed() {
        System.out.println("Sorry, the server crashed\n");
        System.exit(1);
    }

    /**
     * Handles card click. Tries to draw the card and denies the action with an animation if an IllegalActionException is thrown
     */
    @FXML
    void cardClicked(MouseEvent e) {
        GameDTO game = UISession.getGame();
        Group g = (Group) e.getSource();


        try {
            // Find the card
            AnimatedCard c = topRowAnim.stream()
                    .filter(a -> a.getMesh() == g)
                    .findFirst().orElse(null);
            int i;
            if (c != null) {
                i = topRowAnim.indexOf(c);
                // Try to draw it
                UISession.getClient().executeAction(new DrawCardFromTopAction(i));
                drawCard(c);
            } else {
                c = bottomRowAnim.stream()
                        .filter(a -> a.getMesh() == g)
                        .findFirst().orElse(null);
                i = bottomRowAnim.indexOf(c);
                // Try to draw it
                UISession.getClient().executeAction(new DrawCardFromBottomAction(i));
                drawCard(c);
            }
        } catch (Exception ex) {
            // Shake it if the draw was unsuccessful
            Platform.runLater(() -> {
                meshRegistry.get(g).shake();
            });
        }
    }

    /**
     * Handles tile click. Tries to choose the tile
     */
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
                        // anim.getMesh().setTranslateZ(-anim.getCard().getId());
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

            // Move the card at the top of the deck to the deck
            if (topDeck == null) {
                topDeck = new AnimatedCard(game.getBoard().getDeckTribe().get(0), null, deck.localToScene(0, 0).getX(), deck.localToScene(0, 0).getY());
                topDeck.setReference(deck);
                topDeck.getMesh().setTranslateZ(10);
                cardsContainer.getChildren().add(topDeck.getMesh());
            } else {
                try {
                    CardDTO card = game.getBoard().getDeckTribe().get(0);
                    Image back = new Image(getClass().getResource("/back/" + card.getType() + card.getEra().toString() + ".png").toExternalForm());
                    ((PhongMaterial) ((MeshView) topDeck.getMesh().getChildren().get(1)).getMaterial()).setSelfIlluminationMap(back);
                } catch (Exception e) {
                    cardsContainer.getChildren().remove(topDeck.getMesh());
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

                order.resetPosition();
                topDeck.resetPosition();
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

    /**
     * Switches the hand that is being shown
     */
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
