package it.polimi.ingsw;

public class Inventor extends Character {
    private Icon inventionIcon;

    public Inventor(Icon inventionIcon) {
        this.inventionIcon = inventionIcon;
        this.name = "Inventor";
    }

    public Inventor() {
        this.name = "Inventor";
    }

    public Icon getInventionIcon() {
        return inventionIcon;
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

    /**
     * Add an Inventor to the tribe of the specified player
     * @param player
     */
    @Override
    public void addToPlayer(Player player) {
        player.addInventor(this);
    }
}
