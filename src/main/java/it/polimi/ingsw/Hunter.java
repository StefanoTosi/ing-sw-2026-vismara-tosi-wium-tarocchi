package it.polimi.ingsw;

/**
 * Represents the Hunter character.
 * Each Hunter can have an icon that will eventually provide you extra food
 */
public class Hunter extends Character {
    private boolean icon;

    public Hunter() {
        this.name = "Hunter";
        this.icon = false;
    }

    public Hunter(boolean icon) {
        this.name = "Hunter";
        this.icon = icon;
    }

    public boolean getIcon() {
        return icon;
    }

    public void setIcon(boolean icon) {
        this.icon = icon;
    }

    /**
     * Characters doesn't have an era
     * @return null
     */
    @Override
    public Era getEra() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
}
