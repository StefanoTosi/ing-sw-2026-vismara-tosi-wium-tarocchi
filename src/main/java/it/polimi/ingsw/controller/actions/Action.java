package it.polimi.ingsw.controller.actions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;
import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ChooseOfferAction.class, name = "ChooseOffer"),
        @JsonSubTypes.Type(value = ChooseTotemAction.class, name = "ChooseTotem"),
        @JsonSubTypes.Type(value = DrawCardFromBottomAction.class, name = "DrawCardFromBottom"),
        @JsonSubTypes.Type(value = DrawCardFromTopAction.class, name = "DrawCardFromTop"),
        @JsonSubTypes.Type(value = SkipDrawAction.class, name = "SkipDraw")
})

/**
  Represents all possible actions players can attempt during the game.
 */
public interface Action extends Serializable {
    /**
     * Executes the selected action, throwing an exception if the action is not allowed.
     *
     * @param player the player attempting the action
     * @throws IllegalActionException if the player is not allowed to perform the selected action in the current game state
     * @throws IOException if an I/O error occurs during execution with the save file
     */
    void execute(Player player) throws IllegalActionException, IOException;
}