package it.polimi.ingsw;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a player in the game.
 * Each player has a name, a tribe composed by a list of character cards and a list of building cards, an amount of prestige points and food tokens.
 */
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

    /**
     * Adds the specified amount of prestige points, negative parameters allowed.
     * @param pp number of prestige points to add (or subtract if negative)
     */
    public void addPp(int pp) { this.pp += pp; }

    public int getFood() { return food; }

    public void setFood(int food) { this.food = food; }

    /**
     * Adds the specified amount of food, negative parameters allowed.
     * @param food amount of food to add (or subtract if negative)
     */
    public void addFood(int food) { this.food += food; }

    /**
     * Adds a new character card to the list of characters in the tribe.
     * If the new card is a hunter with the food icon, the player gains an amount of food equal to the current number of hunters in the tribe.
     * @param card new character to be added to the tribe
     */
    public void addCharacter(Character card) {
        this.characters.add(card);
        if(card instanceof Hunter && ((Hunter) card).getIcon()){
            this.addFood(this.getNumHunters());
        }
    }

    /**
     * Counts the number of complete sets of six different character cards in the tribe.
     * @return int
     */
    public int countSets() {
        int[] numCharacters = {getNumBuilders(), getNumHunters(), getNumShaman(), getNumInventors(), getNumGatherers(), getNumArtists()};
        return Arrays.stream(numCharacters).min().getAsInt();
    }

    /**
     * Adds a new building card to the list of buildings in the tribe.
     * @param card new building to be added to the tribe
     */
    public void addBuilding(Building card) { this.buildings.add(card); }

    /**
     * Counts the total number of character cards in the tribe.
     * @return int
     */
    public int countNumCharacters() { return characters.size(); }

    /**
     * Counts the total number of building cards in the tribe.
     * @return int
     */
    public int countNumBuildings() { return buildings.size(); }

    /**
     * Counts the number of artists in the tribe.
     * @return int
     */
    public int getNumArtists() {
        int count = 0;

        for (Card card : characters) {
            if (card instanceof Artist) {
                count++;
            }
        }
        return count;
    }

    /**
     * Counts the number of gatherers in the tribe.
     * @return int
     */
    public int getNumGatherers() {
        int count = 0;

        for (Card card : characters) {
            if (card instanceof Gatherer) {
                count++;
            }
        }
        return count;
    }

    /**
     * Counts the number of inventors in the tribe.
     * @return int
     */
    public int getNumInventors() {
        int count = 0;

        for (Card card : characters) {
            if (card instanceof Inventor) {
                count++;
            }
        }
        return count;
    }

    /**
     * Counts the number of hunters in the tribe.
     * @return int
     */
    public int getNumHunters() {
        int count = 0;

        for (Card card : characters) {
            if (card instanceof Hunter) {
                count++;
            }
        }
        return count;
    }

    /**
     * Counts the number of shamans in the tribe.
     * @return int
     */
    public int getNumShaman() {
        int count = 0;

        for (Card card : characters) {
            if (card instanceof Shaman) {
                count++;
            }
        }
        return count;
    }

    /**
     * Counts the number of builders in the tribe.
     * @return int
     */
    public int getNumBuilders() {
        int count = 0;

        for (Card card : characters) {
            if (card instanceof Builder) {
                count++;
            }
        }
        return count;
    }

    /**
     * Counts the total number of stars provided by shamans in the tribe.
     * @return int
     */
    public int getNumStars() {
        int count = 0;

        for (Card card : characters) {
            if (card instanceof Shaman) {
                count += ((Shaman) card).getStars();
            }
        }
        return count;
    }

    /**
     * Calculates the amount of prestige points provided by builders in the tribe at the end of the game.
     * @return int
     */
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
     * Returns total prestige points of the tribe, including both characters and buildings, without calculating any building effect.
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

        for (Card card : characters) {
            if (card instanceof Inventor) {
                found = false;
                currIcon = ((Inventor) card).getInventionIcon();

                for(Icon icon : seenIcons){
                    if (currIcon == icon){
                        found = true;
                    }
                }

                if(!found){
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