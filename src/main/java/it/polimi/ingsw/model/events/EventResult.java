package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.PlayerDTO;
import java.io.Serializable;

/**
 * Contains information about the effect an event has had on a player's food and prestige points.<br>
 * The event is identified by its name and the card's id.
 */
public record EventResult(String event, int cardId, PlayerDTO player, int deltaPp, int deltaFood) implements Serializable {}