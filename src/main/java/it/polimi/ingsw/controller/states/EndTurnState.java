package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.controller.SaveGames;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Offer;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.model.characters.Character;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Game state responsible for handling the end-of-turn phase.
 * <p>
 * During this phase, end-turn effects are resolved, players may perform
 * final draws based on their offer order, and the game transitions to
 * event resolution or the next round.
 * </p>
 */
public class EndTurnState extends GameState {
    private final Game game;
    private List<Player> drawOrder;

    /**
     * Initializes the end-turn state and applies all end-turn effects.
     *
     * <p>
     * This includes resolving building effects that trigger at the end
     * of a turn, followed by preparation of the end-turn flow.
     * </p>
     *
     * @param game the game entering the end-turn phase
     * @throws IllegalActionException if an effect violates game rules
     * @throws IOException if persistence or state update fails
     */
    public EndTurnState(Game game) throws IllegalActionException, IOException {
        this.game = game;

        // Resolve end turn effects
        for (Player p : game.getPlayers()) {
            for (Building b : p.getBuildings()) {
                b.getEffect().applyEffectEndTurn(p);
            }
        }

        resolveEndTurn();
    }

    /**
     * Computes player order for end-turn drawing and determines
     * whether the game proceeds directly to event resolution.
     */
    private void resolveEndTurn() throws IllegalActionException, IOException {
        this.drawOrder = new ArrayList<>(game.getPlayers()
                .stream()
                .sorted((p1, p2) -> p1.getOffer() - p2.getOffer())
                .filter(Player::getCanPickFromTop)
                .toList());

        game.setTurnNumber(game.getTurnNumber() + 1);
        if (!drawOrder.isEmpty()) {
            System.out.println("Resolving end turn phase");
            if(game.getBoard().drawableCardsFromTop(drawOrder.getFirst()) > 0) {
                game.setPlayerTurn(drawOrder.removeFirst());
                game.setState(this);
            } else {
                System.out.println("No cards available to draw");
                game.setPlayerTurn(null);

                ResolveEventsState r = new ResolveEventsState(game);
                r.resolveEvents();
            }
        } else {
            System.out.println("Finished end turn phase");
            game.setPlayerTurn(null);

            ResolveEventsState r = new ResolveEventsState(game);
            r.resolveEvents();
        }
    }

    public StateDTO getStateDTO() {
        return StateDTO.ENDTURN;
    }

    /**
     * Draw a card from the top row, not the Event one
     * @param player
     * @param pos
     * @throws IllegalActionException
     */
    public void drawCardFromTop(Player player, int pos) throws IllegalActionException, IOException {
        if (player.equals(game.getPlayerTurn())) {
            int index = game.getBoard().getTopRowTribe().size();
            if (pos < index) {
                Card character= game.getBoard().drawFromTopRowTribe(pos);
                afterDrawn(player, character);
            } else {
                Building building = game.getBoard().getTopRowBuilding().get(pos-index);
                int buildingCost = building.discountedCost(player);

                if(buildingCost <= player.getFood()) {
                    building = game.getBoard().drawFromTopRowBuilding(pos-index);
                    player.addCard(building);
                    player.addFood(-buildingCost);
                    building.getEffect().whenDrawn(player);
                }
            }

            if (!drawOrder.isEmpty()) {
                game.setPlayerTurn(drawOrder.removeFirst());
            } else {
                // When all players have drawn, transition to ResolveEventsState
                System.out.println("Finished end turn phase");
                game.setPlayerTurn(null);
                game.newTurn();

                ResolveEventsState r = new ResolveEventsState(game);
                SaveGames.saveGame(game.toDTO());
                r.resolveEvents();
            }
        } else {
            throw new IllegalActionException("Player tried to draw a card out of order or more cards than possible");
        }
    }

    /**
     * Lets the player skip if there's no card that interests him in the top row during effect ET2
     * @param player
     * @throws IllegalActionException
     * @throws IOException
     */
    public void skipDraw(Player player) throws IllegalActionException, IOException {
        if (!player.equals(game.getPlayerTurn())) {
            throw new IllegalActionException("Player tried to skip draw out of turn");
        }
        if(!drawOrder.isEmpty()){
            game.setPlayerTurn(drawOrder.removeFirst());
        } else {
            System.out.println("Finished end Turn fase");
            game.setPlayerTurn(null);
            game.newTurn();

            ResolveEventsState r = new ResolveEventsState(game);
            SaveGames.saveGame(game.toDTO());
            r.resolveEvents();
        }
    }

    /**
     *  Manage the effect applied just after the drawn
     * @param player
     * @param character
     */
    private void afterDrawn(Player player, Card character) throws IllegalActionException {
        //TODO: serve davvero questo controllo eventi?
        if (!character.getType().equals("Event")) {
            int tmp_numSets = player.countSets();
            player.addCard(character);
            for (Building building : player.getBuildings()) {
                building.getEffect().applyEffectDraw(player, tmp_numSets, (Character) character);
            }
        } else {
            throw new IllegalActionException("Player tried to draw an event card");
        }
    }
}