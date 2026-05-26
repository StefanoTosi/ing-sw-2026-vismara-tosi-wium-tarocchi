package it.polimi.ingsw.model.exceptions;

/**
 * This exception will be thrown whenever a player tries to execute an invalid or illegal action, which will be specified in its "reason" {@code String}.
 */
public class IllegalActionException extends Exception {
    private final String reason;

    /**
     * Generates an {@code IllegalActionException} with the specified reason.
     * @param reason the {@code String} describing the illegal or invalid action
     */
    public IllegalActionException(String reason) {
        super(reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
