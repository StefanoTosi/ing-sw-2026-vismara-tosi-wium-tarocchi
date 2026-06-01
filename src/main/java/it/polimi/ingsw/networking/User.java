package it.polimi.ingsw.networking;

/**
 * Represents a user of the system identified by a nickname,
 * with authentication data and session state information.<br>
 *<br>
 * A user can be active (logged in) and/or currently participating
 * in a game session.
 */
public class User {
    private String nickname;
    private String password;
    private boolean active;
    private boolean inGame;

    /**
     * Creates a user with only a nickname.<br>
     * The password is initially unset and the user is inactive.<br>
     *<br>
     * Used when the DB is unreachable and there are saved games
     * @param nickname the user's nickname
     */
    public User(String nickname){
        this.nickname = nickname;
        this.password = null;
        this.active = false;
        this.inGame = false;
    }

    /**
     * Creates a user with nickname and password.<br>
     * The user is initially inactive and not in a game.<br>
     *<br>
     * Used to load the users from the DB
     * @param nickname the user's nickname
     * @param password the user's password
     */
    public User(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
        this.active = false;
        this.inGame = false;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Sets whether the user is currently active (logged in).
     *
     * @param active true if the user is active, false otherwise
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns whether the user is currently active.
     *
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Returns whether the user is currently in a game.
     *
     * @return true if the user is in a game, false otherwise
     */
    public boolean getInGame(){
        return this.inGame;
    }

    /**
     * Sets whether the user is currently in a game.
     *
     * @param inGame true if the user is in a game, false otherwise
     */
    public void setInGame(boolean inGame){
        this.inGame = inGame;
    }
}