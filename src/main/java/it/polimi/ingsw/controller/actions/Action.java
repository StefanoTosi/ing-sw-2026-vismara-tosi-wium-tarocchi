package it.polimi.ingsw.controller.actions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * Represents a generic game action that can be executed by a player.
 * <p>
 * Implementations encapsulate a specific command sent by the client and
 * executed on the server-side game model.
 * </p>
 *
 * <p>
 * Jackson polymorphic annotations are used to support serialization and
 * deserialization of concrete action types exchanged through the network.
 * Each implementation is identified by the {@code type} property.
 * </p>
 */
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

public interface Action extends Serializable {

    /**
     * Executes the action on behalf of the specified player.
     * <p>
     * Implementations are responsible for validating the action according
     * to the current game state and applying any resulting changes to the
     * game model.
     * </p>
     *
     * @param player the player performing the action
     * @throws IllegalActionException if the action is not allowed in the
     * current game state
     * @throws IOException if an I/O error occurs during action execution
     */
    void execute(Player player) throws IllegalActionException, IOException;
}