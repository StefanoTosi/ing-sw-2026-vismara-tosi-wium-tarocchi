package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Card> tribe;
    private int pp;
    private int food;

    public Player(String name, List<Card> tribe, int pp, int food) {
        this.name = name;
        this.tribe = tribe;
        this.pp = pp;
        this.food = food;
    }

    public String getName() { return name; }

    public int getPp() { return pp; }

    public void setPp(int pp) { this.pp = pp; }

    public int getFood() { return food; }

    public void setFood(int food) { this.food = food; }

    public void addCard(Card card) {this.tribe.add(card);}

    public int countNumTribe() { return tribe.size(); }

    public int getNumArtists() {
        int count = 0;

        for (Card card : tribe) {
            if (card instanceof Artist) {
                count++;
            }
        }
        return count;
    }

    public int getNumGatherers() {
        int count = 0;

        for (Card card : tribe) {
            if (card instanceof Gatherer) {
                count++;
            }
        }
        return count;
    }

    public int getNumInventors() {
        int count = 0;

        for (Card card : tribe) {
            if (card instanceof Inventor) {
                count++;
            }
        }
        return count;
    }

    public int getNumStars() {
        int count = 0;

        for (Card card : tribe) {
            if (card instanceof Shaman) {
                count += ((Shaman) card).getStars();
            }
        }
        return count;
    }

    public int countTribePp() {
        int points = 0;
        int numIcons = 0;

        points += this.getNumArtists() * 2;

        for (Card card : tribe) {
            if (card instanceof Builder) {
                points += ((Builder) card).getPp();
            }
        }

        //manca solo conteggio icone distinte
        for (Card card : tribe) {
            if (card instanceof Inventor) {

            }
        }
        points += numIcons * this.getNumInventors();

        return points;
    }

}
