package it.polimi.ingsw.controller.actions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ChooseOfferAction.class, name = "ChooseOffer"),
        @JsonSubTypes.Type(value = ChooseTotemAction.class, name = "ChooseTotem"),
        @JsonSubTypes.Type(value = DrawCardFromBottomAction.class, name = "DrawCardFromBottom"),
        @JsonSubTypes.Type(value = DrawCardFromTopAction.class, name = "DrawCardFromTop")
})

public interface Action extends Serializable {
    void execute(Player player) throws IllegalActionException, IOException;

}
