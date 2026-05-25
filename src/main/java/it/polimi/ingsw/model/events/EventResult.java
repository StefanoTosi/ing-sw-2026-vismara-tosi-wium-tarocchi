package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.PlayerDTO;
import java.io.Serializable;

/**
 * Contains information about the effect an event has had on a player's food and pp
 */
public record EventResult(String event, int cardId, PlayerDTO player, int deltaPp, int deltaFood) implements Serializable {}