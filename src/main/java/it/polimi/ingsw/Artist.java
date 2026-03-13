package it.polimi.ingsw;

public class Artist extends Character {

    public Artist() {
        this.name = "Artist";
    }

    @Override
    public Era getEra() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
}
