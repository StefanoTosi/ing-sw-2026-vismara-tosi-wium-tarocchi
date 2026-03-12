package it.polimi.ingsw;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static java.util.Collections.min;

public class Player {
    private String name;
    private List<Character> characters;
    private List<Building> buildings;
    private int pp;
    private int food;

    public Player(String name, List<Character> characters, List<Building> buildings, int pp, int food) {
        this.name = name;
        this.characters = characters;
        this.buildings = buildings;
        this.pp = pp;
        this.food = food;
    }

    public String getName() { return name; }

    public int getPp() { return pp; }

    public void setPp(int pp) { this.pp = pp; }

    public void addPp(int pp) { this.pp += pp; }

    public int getFood() { return food; }

    public void setFood(int food) { this.food = food; }

    public void addFood(int food) { this.food += food; }

    //aggiungere Hunter food bonus
    public void addCharacter(Character card) {
        this.characters.add(card);
        if(card instanceof Hunter){

        }
    }

    public int countSets() {
        int[] numCharacters = {getNumBuilders(), getNumHunters(), getNumShaman(), getNumInventors(), getNumGatherers(), getNumArtists()};
        return Arrays.stream(numCharacters).min().getAsInt();
    }

    public void addBuilding(Building card) { this.buildings.add(card); }

    public int countNumTribe() { return characters.size(); }

    public int getNumArtists() {
        int count = 0;

        for (Card card : characters) {
            if (card instanceof Artist) {
                count++;
            }
        }
        return count;
    }

    public int getNumGatherers() {
        int count = 0;

        for (Card card : characters) {
            if (card instanceof Gatherer) {
                count++;
            }
        }
        return count;
    }

    public int getNumInventors() {
        int count = 0;

        for (Card card : characters) {
            if (card instanceof Inventor) {
                count++;
            }
        }
        return count;
    }

    public int getNumHunters() {
        int count = 0;

        for (Card card : characters) {
            if (card instanceof Hunter) {
                count++;
            }
        }
        return count;
    }

    public int getNumShaman() {
        int count = 0;

        for (Card card : characters) {
            if (card instanceof Shaman) {
                count++;
            }
        }
        return count;
    }

    public int getNumBuilders() {
        int count = 0;

        for (Card card : characters) {
            if (card instanceof Builder) {
                count++;
            }
        }
        return count;
    }

    public int getNumStars() {
        int count = 0;

        for (Card card : characters) {
            if (card instanceof Shaman) {
                count += ((Shaman) card).getStars();
            }
        }
        return count;
    }

    public int countBuildersPp() {
        int points = 0;
        for (Card card : characters) {
            if (card instanceof Builder) {
                points += ((Builder) card).getPp();
            }
        }
        return points;
    }

    /**
     * Returns total prestige points of the tribe, including both characters and buildings, without calculating any effect.
     * @return int
     */
    public int countTribePp() {
        int points = 0;
        int numIcons = 0;
        Icon currIcon = null;
        List<Icon> seenIcons = new ArrayList<>();
        boolean found = false;

        points += this.getNumArtists() * 2;
        points += this.countBuildersPp();

        //manca conteggio icone distinte
        for (Card card : characters) {
            if (card instanceof Inventor) {
                found = false;
                currIcon = ((Inventor) card).getInventionIcon();

                for(Icon icon : seenIcons){
                    if (currIcon == icon){
                        found = true;
                    }
                }

                if(found == false){
                    seenIcons.add(currIcon);
                    numIcons++;
                }
            }
        }
        points += numIcons * this.getNumInventors();

        for(Building building : buildings) {
            points += building.getPp();
        }

        return points;
    }

}