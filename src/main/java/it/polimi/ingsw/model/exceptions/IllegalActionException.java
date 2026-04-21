package it.polimi.ingsw.model.exceptions;

/**
 * This exception will be thrown when a plyer tries to execute an invalid or illegal action
 */
public class IllegalActionException extends Exception {
    private final String reason;

    public IllegalActionException(String reason) {
        super(reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
