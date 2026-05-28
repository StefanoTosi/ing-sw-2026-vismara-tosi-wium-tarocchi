package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IllegalActionException;
import java.util.List;

/**
 * Represents common elements of all {@code Events}.<br>
 * Possible event subtypes can be {@code CavePaintings}, {@code Hunt}, {@code ShamanicRitual} and {@code Sustenance}.
 */
public abstract class Event extends Card {
    protected String name;

    /**
     * Generates an {@code Event} card, belonging to the specified {@code Era}.
     * @param era the {@code Era} the card belongs to
     */
    public Event(Era era) {
        super(era);
        this.TYPE = "Event";
    }

    /**
     * Resolves the {@code Event} effect.
     * @param players the list of {@code Players} involved in the {@code Event}
     * @return the list of {@code EventResults}, containing the food & pps deltas for each player
     */
    public List<EventResult> applyEffect(List<Player> players) {
        return null;
    }

    /**
     * Throws an exception when a {@code Player} tries to draw an {@code Event} card.
     * @param player the player who is trying to draw the card
     * @throws IllegalActionException if a player tries to draw an {@code Event} card
     */
    @Override
    public void addToPlayer(Player player) throws IllegalActionException {
        throw new IllegalActionException("Player tried to draw an event");
    }
}
