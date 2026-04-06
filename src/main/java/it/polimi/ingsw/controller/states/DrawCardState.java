package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.Hunter;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

/**
 *
 */
public class DrawCardState extends GameState {
    private final Game game;

    public DrawCardState(Game game) {
        this.game = game;
    }

    /**
     *
     * @param player
     * @param pos
     * @throws IllegalActionException
     */
    public void drawCardFromTop(Player player, int pos) throws IllegalActionException {
        int index = game.getBoard().getBottomRowTribe().size();
        if (pos < index) {
            Card character= game.getBoard().drawFromTopRowTribe(pos);
            afterDrawn(player, character);
        }else{
            Building building = game.getBoard().drawFromTopRowBuilding(pos-index);
            player.addCard(building);
            building.getEffect().whenDrawn(player);
        }
    }

    /**
     *
     * @param player
     * @param pos
     * @throws IllegalActionException
     */
    public void drawCardFromBottom(Player player, int pos) throws IllegalActionException {
        int index = game.getBoard().getBottomRowTribe().size();
        if (pos < index) {
            Card character =  game.getBoard().drawFromBottomRowTribe(pos);
            afterDrawn(player, character);
        }else{
            Building building = game.getBoard().drawFromBottomRowBuilding(pos-index);
            player.addCard(building);
            building.getEffect().whenDrawn(player);
        }
    }

    /**
     *
     * @param player
     * @param character
     */
    private void afterDrawn(Player player, Card character){
        if(!character.getType().equals("Event")) {
            int tmp_numSets = player.countSets();
            player.addCard(character);
            for(Building building : player.getBuildings()){
                building.getEffect().applyEffectDraw(player, tmp_numSets, (Character)character);
            }
            if (character.getName().equals("Hunter")) {
                huntersDraft(player, (Hunter) character);
            }
        }
    }

    /**
     *
     * @param player
     * @param hunter
     */
    private void huntersDraft(Player player, Hunter hunter) {
        if(hunter.getIcon()){
            player.addFood(player.getNumHunters());
        }
    }
}
