package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.controller.SaveGames;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Offer;
import it.polimi.ingsw.model.board.Order;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.events.Event;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Game state responsible for the card-drawing phase of a round.
 * <p>
 * During this state, players take turns drawing cards from the board
 * according to their offer tile order. Each player may draw a limited
 * number of cards from the top and bottom rows, and must respect
 * board constraints and resource requirements.
 * </p>
 *
 * <p>
 * When all players have finished drawing, the state transitions to
 * {@link EndTurnState}.
 * </p>
 */
public class DrawCardState extends GameState {
    private final Game game;
    private int drawTopCount;
    private int drawBottomCount;
    private List<Player> drawOrder;


    /**
     * Creates a new DrawCardState and initializes the drawing phase.
     * <p>
     * Players are ordered according to the offer tile they selected,
     * and the first eligible player immediately starts the card-drawing turn.
     * </p>
     *
     * @param game the current game instance
     * @throws IllegalActionException if an invalid game action occurs while starting the phase
     * @throws IOException if an error occurs while handling game persistence
     */
    public DrawCardState(Game game) throws IllegalActionException, IOException {
        this.game = game;
        this.drawTopCount = 0;
        this.drawBottomCount = 0;

        drawOrder = new ArrayList<>(game.getPlayers()
                .stream()
                .sorted((p1, p2) -> p1.getOffer() - p2.getOffer())
                .toList());

        startDrawingTurn();
    }

    public StateDTO getStateDTO() {
        return StateDTO.DRAWCARD;
    }

    /**
     * Draw a card from the top row,
     * not an Event one
     * @param player
     * @param pos
     * @throws IllegalActionException
     */
    public void drawCardFromTop(Player player, int pos) throws IllegalActionException, IOException {
        Offer offer = player.getGame().getBoard().getOfferPath()
                .stream()
                .filter(o -> o.getOrder() == player.getOffer())
                .findFirst().get();
        if (player.equals(game.getPlayerTurn()) && drawTopCount < offer.getDrawTop()) {
            int index = game.getBoard().getTopRowTribe().size();
            if (pos < index) {
                Card character = game.getBoard().drawFromTopRowTribe(pos);
                afterDrawn(player, character);
                game.getBoard().getTopRowTribe().remove(character);
            } else {
                Building building = game.getBoard().getTopRowBuilding().get(pos-index);
                int buildingCost = building.discountedCost(player);

                if (buildingCost <= player.getFood()) {
                    building = game.getBoard().drawFromTopRowBuilding(pos-index);
                    player.addCard(building);
                    game.getBoard().getTopRowBuilding().remove(building);
                    player.addFood(-buildingCost);
                    building.getEffect().whenDrawn(player);
                } else {
                    throw new IllegalActionException("Player tried to draw a building but has insufficient food");
                }
            }

            drawTopCount++;

            //TODO: throw an exception/print when no cards are available

            // When all cards have been drawn or there are no more cards available, go to the next player
            transitionIfNeeded(player);

        } else {
            throw new IllegalActionException("Player tried to draw a card out of order or more cards than possible (" + offer.getDrawTop() + ")");
        }
    }

    /**
     * Draw a card from the bottom row,
     * not an Event one
     * @param player
     * @param pos
     * @throws IllegalActionException
     */
    public void drawCardFromBottom(Player player, int pos) throws IllegalActionException, IOException {
        Offer offer = player.getGame().getBoard().getOfferPath()
                .stream()
                .filter(o -> o.getOrder() == player.getOffer())
                .findFirst().get();
        if (player.equals(game.getPlayerTurn()) && drawBottomCount < offer.getDrawBottom()) {
            int index = game.getBoard().getBottomRowTribe().size();
            if (pos < index) {
                Card character = game.getBoard().drawFromBottomRowTribe(pos);
                afterDrawn(player, character);
                game.getBoard().getBottomRowTribe().remove(character);
            } else {
                Building building = game.getBoard().getBottomRowBuilding().get(pos-index);
                int buildingCost = building.discountedCost(player);

                if(buildingCost <= player.getFood()) {
                    building = game.getBoard().drawFromBottomRowBuilding(pos-index);
                    player.addCard(building);
                    game.getBoard().getBottomRowBuilding().remove(building);
                    player.addFood(-buildingCost);
                    building.getEffect().whenDrawn(player);
                } else {
                    throw new IllegalActionException("Player tried to draw a building but has insufficient food");
                }
            }

            drawBottomCount += 1;

            // When all cards have been drawn, go to the next player
            transitionIfNeeded(player);

        } else {
            throw new IllegalActionException("Player tried to draw a card out of order or more cards than possible (" + offer.getDrawBottom() + ")");
        }
    }

    /**
     *  Manage the effect applied just after the drawn
     * @param player
     * @param character
     */
    private void afterDrawn(Player player, Card character) throws IllegalActionException {
        int tmp_numSets = player.countSets();
        player.addCard(character);
        for (Building building : player.getBuildings()) {
            building.getEffect().applyEffectDraw(player, tmp_numSets, (Character)character);
        }
    }

    /**
     * Starts the drawing turn for the next eligible player.
     * <p>
     * The player with the highest-priority available offer tile is selected.
     * Players who cannot draw any cards are automatically skipped, receive
     * any applicable bonuses, and return their totem to the order track.
     * </p>
     *
     * <p>
     * If no player can draw cards, the game transitions to
     * {@link EndTurnState}.
     * </p>
     *
     * @throws IllegalActionException if an invalid action is detected
     * @throws IOException if an error occurs during state transition or persistence
     */
    private void startDrawingTurn() throws IllegalActionException, IOException {
        game.setPlayerTurn(drawOrder.removeFirst());

        // Tile A does not allow you to draw any cards
        //TODO: può essere eliminato se compreso nel controllo successivo?
        if (game.getPlayerTurn().getOffer() == 'A') {
            game.getPlayerTurn().addFood(game.getBoard().getOfferPath().get(0).getFoodBonus());
            moveBackToOrderTile();
            game.setPlayerTurn(drawOrder.removeFirst());
        }

        // Verify if at least one player can draw cards
        boolean found = false;
        while (!found && !drawOrder.isEmpty()) {
            if (game.getBoard().playerCanDraw(drawOrder.getFirst(), drawTopCount, drawBottomCount)) {
                found = true;
            } else {
                System.out.println("No cards available to draw");
                game.setPlayerTurn(drawOrder.removeFirst());
                moveBackToOrderTile();
                drawTopCount = 0;
                drawBottomCount = 0;
            }
        }

        // If no player can draw any card, transition to next state
        if(drawOrder.isEmpty()) {
            game.setPlayerTurn(null);
            EndTurnState e = new EndTurnState(game);
        }
    }

    /**
     * Moves totem back to the order tile and applies building ET1 effect if needed
     */
    public void moveBackToOrderTile() {
        // Move back to the order tile
        int availableOrder = (int) game.getPlayers()
                .stream()
                .filter(p -> p.getOffer() == '\0')
                .count();

        game.getPlayerTurn().setOrder(availableOrder);
        game.getPlayerTurn().setOffer('\0');

        // Apply food bonus
        Order o = game.getBoard().getOrder();
        if (o.getFoodBonus(availableOrder) + game.getPlayerTurn().getFood() >= 0) {
            game.getPlayerTurn().addFood(o.getFoodBonus(availableOrder));
        } else {
            game.getPlayerTurn().setFood(0);
            game.getPlayerTurn().addPp(o.getPpBonus(availableOrder));
        }

        // Execute the ET1 effect
        game.getPlayerTurn().getBuildings()
                .stream()
                .forEach(b -> b.getEffect().applyEffectTileBonus(game.getPlayerTurn(), b));
    }

    /**
     * In the case there are only buildings to draw lets the player skip the draw turn
     * @param player
     * @throws IllegalActionException
     * @throws IOException
     */
    public void skipDraw(Player player) throws IllegalActionException, IOException {
        if (!player.equals(game.getPlayerTurn())) {
            throw new IllegalActionException("Player tried to skip draw out of turn");
        }

        Offer offer = player.getGame().getBoard().getOfferPath().stream().filter(o -> o.getOrder() == player.getOffer()).findFirst().get();
        boolean canSkipTop = offer.getDrawTop() > drawTopCount && checkRow(game.getBoard().getTopRowTribe());
        boolean canSkipBottom = offer.getDrawBottom() > drawBottomCount && checkRow(game.getBoard().getBottomRowTribe());
        boolean noDrawsLeft = !game.getBoard().playerCanDraw(player, drawTopCount, drawBottomCount);

        if (canSkipBottom || canSkipTop || noDrawsLeft) {
            drawTopCount = 0;
            drawBottomCount = 0;
            startDrawingTurn();
        } else {
            throw new IllegalActionException("Player tried to skip draw but valid cards are available");
        }

    }

    /**
     * Checks whether all cards in a row are {@link Event} cards.
     * <p>
     * Event cards cannot be drawn during this phase, therefore a row
     * containing only events is considered unavailable for drawing.
     * </p>
     *
     * @param row the row of cards to inspect
     * @return {@code true} if every card in the row is an event,
     *         {@code false} otherwise
     */
    private boolean checkRow(List<Card> row){
        return row.stream().allMatch(card -> card instanceof Event);
    }

    /**
     * Verifies whether the current player can continue drawing cards.
     * <p>
     * If no further draws are possible, the player's draw counters are reset,
     * the totem is moved back to the order track, and control is passed to the
     * next player. When all players have completed the drawing phase, the game
     * is saved and transitions to {@link EndTurnState}.
     * </p>
     *
     * @param player the player whose draw availability is being checked
     * @throws IllegalActionException if an invalid action occurs during the transition
     * @throws IOException if an error occurs while saving the game or changing state
     */
    private void transitionIfNeeded(Player player) throws IllegalActionException, IOException {
        if (!game.getBoard().playerCanDraw(player, drawTopCount, drawBottomCount)) {
            drawTopCount = 0;
            drawBottomCount = 0;

            moveBackToOrderTile();

            // Go to next player or next state
            if (!drawOrder.isEmpty()) {
                game.setPlayerTurn(drawOrder.removeFirst());
            } else {
                // When all players have drawn, transition to EndTurnState
                System.out.println("Finished drawing cards");
                game.setPlayerTurn(null);

                SaveGames.saveGame(game.toDTO());
                EndTurnState e = new EndTurnState(game);
            }
        }
    }
}
