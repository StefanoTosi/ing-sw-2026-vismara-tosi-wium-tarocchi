package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import it.polimi.ingsw.model.characters.Character;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EndTurnState extends GameState {
    private final Game game;
    private List<Player> drawOrder;

    EndTurnState(Game game) throws IllegalActionException, RemoteException {
        // Resolve end turn effects
        for (Player p : game.getPlayers()) {
            for (Building b : p.getBuildings()) {
                b.getEffect().applyEffectEndTurn(p);
            }
        }

        this.game = game;
        this.drawOrder = new ArrayList<>(game.getPlayers()
                .stream()
                .sorted((p1, p2) -> p1.getOffer() - p2.getOffer())
                .filter(p -> p.getCanPickFromTop())
                .toList());

        game.setTurnNumber(game.getTurnNumber() + 1);
        if (drawOrder.size() > 0) {
            game.setPlayerTurn(drawOrder.remove(0));
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
                Building building = game.getBoard().drawFromTopRowBuilding(pos-index);
                player.addCard(building);
                building.getEffect().whenDrawn(player);
            }

            if (drawOrder.size() > 0) {
                game.setPlayerTurn(drawOrder.remove(0));
            } else {
                // When all players have draw, transition to FillBoardState
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
