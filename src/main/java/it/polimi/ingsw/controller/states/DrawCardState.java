package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Offer;
import it.polimi.ingsw.model.characters.Builder;
import it.polimi.ingsw.model.characters.Hunter;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DrawCardState extends GameState {
    private final Game game;
    private int drawTopCount;
    private int drawBottomCount;
    private List<Player> drawOrder;

    public DrawCardState(Game game) throws IllegalActionException, RemoteException {
        this.game = game;
        this.drawTopCount = 0;
        this.drawBottomCount = 0;

        drawOrder = new ArrayList<>(game.getPlayers()
                .stream()
                .sorted((p1, p2) -> p1.getOffer() - p2.getOffer())
                .toList());

        game.setPlayerTurn(drawOrder.removeFirst());

        // Tile A does not allow you to draw any cards
        //TODO: può essere eliminato se compreso nel controllo successivo?
        if (game.getPlayerTurn().getOffer() == 'A') {
            game.setPlayerTurn(drawOrder.removeFirst());
        }

        // Verify if at least one player can draw cards
        boolean found = false;
        while(!found && !drawOrder.isEmpty()) {
            if(game.getBoard().playerCanDraw(drawOrder.getFirst(), drawTopCount, drawBottomCount)) {
                found = true;
            } else {
                System.out.println("No cards available to draw");
                game.setPlayerTurn(drawOrder.removeFirst());
                // Move back to the order tile
                int availableOrder = (int) game.getPlayers()
                        .stream()
                        .filter(p -> p.getOffer() == '\0')
                        .count();

                game.getPlayerTurn().setOrder(availableOrder);
                game.getPlayerTurn().setOffer('\0');

                // Execute the ET1 effect
                game.getPlayerTurn().getBuildings()
                        .stream()
                        .forEach(b -> b.getEffect().applyEffectTileBonus(game.getPlayerTurn(), b));
            }
        }

        // If no player can draw any card, transition to next state
        if(drawOrder.isEmpty()) {
            game.setPlayerTurn(null);
            ResolveEventsState r = new ResolveEventsState(game);
            r.resolveEvents();
        }
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
    public void drawCardFromTop(Player player, int pos) throws IllegalActionException, RemoteException {
        Offer offer = player.getGame().getBoard().getOfferPath()
                .stream()
                .filter(o -> o.getOrder() == player.getOffer())
                .findFirst().get();
        if (player.equals(game.getPlayerTurn()) && drawTopCount < offer.getDrawTop()) {
            int index = game.getBoard().getTopRowTribe().size();
            if (pos < index) {
                Card character = game.getBoard().drawFromTopRowTribe(pos);
                afterDrawn(player, character);
            } else {
                Building building = game.getBoard().getTopRowBuilding().get(pos-index);
                int buildingCost = building.discountedCost(player);

                if(buildingCost <= player.getFood()) {
                    building = game.getBoard().drawFromTopRowBuilding(pos-index);
                    player.addCard(building);
                    player.addFood(-buildingCost);
                    building.getEffect().whenDrawn(player);
                } else {
                    throw new IllegalActionException("Player " + player.getName() + " tried to draw a building but has insufficient food");
                }
            }

            drawTopCount++;

            //TODO: throw an exception/print when no cards are available

            // When all cards have been drawn or there are no more cards available, go to the next player
            transitionIfNeeded(player);

        } else {
            throw new IllegalActionException("Player " + player.getName() + " tried to draw a card out of order or more cards than possible (" + offer.getDrawTop() + ")");
        }
    }

    /**
     * Draw a card from the bottom row,
     * not an Event one
     * @param player
     * @param pos
     * @throws IllegalActionException
     */
    public void drawCardFromBottom(Player player, int pos) throws IllegalActionException, RemoteException {
        Offer offer = player.getGame().getBoard().getOfferPath()
                .stream()
                .filter(o -> o.getOrder() == player.getOffer())
                .findFirst().get();
        if (player.equals(game.getPlayerTurn()) && drawBottomCount < offer.getDrawBottom()) {
            int index = game.getBoard().getBottomRowTribe().size();
            if (pos < index) {
                Card character = game.getBoard().drawFromBottomRowTribe(pos);
                afterDrawn(player, character);
            } else {
                Building building = game.getBoard().getBottomRowBuilding().get(pos-index);
                int buildingCost = building.discountedCost(player);

                if(buildingCost <= player.getFood()) {
                    building = game.getBoard().drawFromBottomRowBuilding(pos-index);
                    player.addCard(building);
                    player.addFood(-buildingCost);
                    building.getEffect().whenDrawn(player);
                } else {
                    throw new IllegalActionException("Player " + player.getName() + " tried to draw a building but has insufficient food");
                }
            }

            drawBottomCount += 1;

            // When all cards have been drawn, go to the next player
            transitionIfNeeded(player);

        } else {
            throw new IllegalActionException("Player " + player.getName() + " tried to draw a card out of order or more cards than possible (" + offer.getDrawBottom() + ")");
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

    private void transitionIfNeeded(Player player) throws IllegalActionException, RemoteException {
        if (!game.getBoard().playerCanDraw(player, drawTopCount, drawBottomCount)) {
            drawTopCount = 0;
            drawBottomCount = 0;

            // Move back to the order tile
            int availableOrder = (int) game.getPlayers()
                    .stream()
                    .filter(p -> p.getOffer() == '\0')
                    .count();

            game.getPlayerTurn().setOrder(availableOrder);
            game.getPlayerTurn().setOffer('\0');

            // Execute the ET1 effect
            game.getPlayerTurn().getBuildings()
                    .stream()
                    .forEach(b -> b.getEffect().applyEffectTileBonus(game.getPlayerTurn(), b));

            // Go to next player or next state
            if (!drawOrder.isEmpty()) {
                game.setPlayerTurn(drawOrder.removeFirst());
            } else {
                // When all players have drawn, transition to ResolveEventsState
                System.out.println("Finished drawing cards");
                game.setPlayerTurn(null);

                ResolveEventsState r = new ResolveEventsState(game);
                r.resolveEvents();
            }
        }
    }
}
