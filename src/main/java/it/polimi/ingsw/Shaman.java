package it.polimi.ingsw;

/**
 * Represents the Shaman character
 */
public class Shaman extends Character {
    private int stars;

    public Shaman(int stars) {
        this.stars = stars;
        this.name = "Shaman";
    }

    public Shaman() {
        this.name = "Shaman";
    }

    public int getStars() {
        return stars;
    }
    public void setStars(int stars) {
        this.stars = stars;
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
