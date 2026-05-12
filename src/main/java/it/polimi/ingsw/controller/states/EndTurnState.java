package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.model.characters.Character;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class EndTurnState extends GameState {
    private final Game game;
    private List<Player> drawOrder;

    public EndTurnState(Game game) throws IllegalActionException, RemoteException {
        this.game = game;

        // Resolve end turn effects
        for (Player p : game.getPlayers()) {
            for (Building b : p.getBuildings()) {
                b.getEffect().applyEffectEndTurn(p);
            }
        }

        resolveEndTurn();
    }

    public EndTurnState(Game game, List<Player> drawOrder) throws IllegalActionException, RemoteException {
        this.game = game;
        this.drawOrder = drawOrder;

        // Resolve end turn effects
        for (Player p : game.getPlayers()) {
            for (Building b : p.getBuildings()) {
                b.getEffect().applyEffectEndTurn(p);
            }
        }

        resolveEndTurn();
    }

    private void resolveEndTurn() throws IllegalActionException, RemoteException {
        this.drawOrder = new ArrayList<>(game.getPlayers()
                .stream()
                .sorted((p1, p2) -> p1.getOffer() - p2.getOffer())
                .filter(p -> p.getCanPickFromTop())
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

                FillBoardState f = new FillBoardState(game);
                f.refillBoard();
            }
        } else {
            System.out.println("Finished end turn phase");
            game.setPlayerTurn(null);

            FillBoardState f = new FillBoardState(game);
            f.refillBoard();
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
    public void drawCardFromTop(Player player, int pos) throws IllegalActionException, RemoteException {
        Game game = player.getGame();

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
                // When all players have drawn, transition to FillBoardState
                System.out.println("Finished end turn phase");
                game.setPlayerTurn(null);
                game.newTurn();

                FillBoardState f = new FillBoardState(game);
                f.refillBoard();
            }
        } else {
            throw new IllegalActionException("Player tried to draw a card out of order or more cards than possible");
        }
    }

    /**
     *  Manage the effect applied just after the drawn
     * @param player
     * @param character
     */
    private void afterDrawn(Player player, Card character) throws IllegalActionException {
        if (!character.getType().equals("Event")) {
            int tmp_numSets = player.countSets();
            player.addCard(character);
            for (Building building : player.getBuildings()) {
                building.getEffect().applyEffectDraw(player, tmp_numSets, (Character) character);
            }
        } else {
            throw new IllegalActionException("Player " + player.getName() + " tried to draw an event card");
        }
    }
}
