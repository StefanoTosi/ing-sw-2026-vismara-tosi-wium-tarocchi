package it.polimi.ingsw.networking;

public class User {
    private String nickname;
    private String password;
    private boolean active;

    public User(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
        this.active = false;
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

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return this.active;
    }
}