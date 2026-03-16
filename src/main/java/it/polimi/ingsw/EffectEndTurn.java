package it.polimi.ingsw;


/**
 * Represent the Buildings which have effects to use every end of the turn
 */
public class EffectEndTurn extends Effect{

    public EffectEndTurn(){}

    /**
     * Add the drawn card to the player's deck
     * @param player the player who has the Building card with the effect
     * @param card the card draw from the top row, it has to be of type character or building
     */
    public void applyEffectEndTurn(Player player, Card card){
        if(card.getType().equals("Character")){
            player.addCharacter((Character) card);
        }else{
            player.addBuilding((Building) card);
        }
    }

}
