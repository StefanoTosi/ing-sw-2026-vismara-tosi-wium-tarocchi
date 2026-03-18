package it.polimi.ingsw;

/**
 * Represents the Hunter character.
 * Each Hunter can have an icon that will eventually provide you extra food
 */
public class Hunter extends Character {
    private boolean icon;

    public Hunter(boolean icon, Era era) {
        this.name = "Hunter";
        this.icon = icon;
        this.era = era;
    }

    public boolean getIcon() {
        return icon;
    }

    @Override
    public Era getEra() {
        return era;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Add a Hunter to the tribe of the specified player
     * @param player
     */
    @Override
    public void addToPlayer(Player player) {
        player.addHunter(this);
    }
}
