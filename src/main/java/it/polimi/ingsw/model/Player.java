package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a player in the game.<br>
 * Each player has a name, a tribe composed by a list of character cards and a list of building cards, an amount of prestige points and food tokens.
 */
public class Player {
    private final String name;

    private List<Artist> artists;
    private List<Gatherer> gatherers;
    private List<Hunter> hunters;
    private List<Inventor> inventors;
    private List<Shaman> shamans;
    private List<Builder> builders;
    private List<Building> buildings;

    private int pp;
    private int food;

    private int foodDiscount;
    private int additionalStars;
    private boolean dontLosePp;
    private boolean doublePp;
    private boolean canPickFromTop;

    private int order; // Indicates the position of player on the order tile
    private char offer; // Indicates the position of player on the offer path
    private Totem totem;

    private Game game;

    /**
     * Generates a new player, with empty tribe and variables.
     * @param name the player's name
     */
    public Player(String name) {
        this.name = name;

        this.artists = new ArrayList<>();
        this.gatherers = new ArrayList<>();
        this.hunters = new ArrayList<>();
        this.inventors = new ArrayList<>();
        this.shamans = new ArrayList<>();
        this.builders = new ArrayList<>();
        this.buildings = new ArrayList<>();

        this.pp = 0;
        this.food = 0;


        this.foodDiscount = 0;
        this.additionalStars = 0;
        this.dontLosePp = false;
        this.doublePp = false;
        this.canPickFromTop = false;

        this.order = 0;
        this.offer = '\0';
        this.totem = null;

        this.game = null;
    }

    /**
     * Generates a {@code Player} and initializes their tribe and variables with given parameters.
     * @param name the player's name
     * @param artists the list of {@code Artists} in the player's tribe
     * @param gatherers the list of {@code Gatherers} in the player's tribe
     * @param hunters the list of {@code Hunters} in the player's tribe
     * @param inventors the list of {@code Inventors} in the player's tribe
     * @param shamans the list of {@code Shamans} in the player's tribe
     * @param builders the list of {@code Builders} in the player's tribe
     * @param buildings the list of {@code Buildings} in the player's tribe
     * @param pp the player's current prestige points
     * @param food the player's current food tokens
     * @param order the player's current position on the {@code Order} tile
     * @param offer the player's current {@code Offer} tile
     * @param totem the player's chosen {@code Totem} color
     */
    public Player(String name, List<Artist> artists, List<Gatherer> gatherers, List<Hunter> hunters, List<Inventor> inventors, List<Shaman> shamans, List<Builder> builders,
                  List<Building> buildings, int pp, int food, int order, char offer, Totem totem) {
        this.name = name;

        this.artists = artists;
        this.gatherers = gatherers;
        this.hunters = hunters;
        this.inventors = inventors;
        this.shamans = shamans;
        this.builders = builders;
        this.buildings = buildings;

        this.pp = pp;
        this.food = food;

        this.foodDiscount = 0;
        this.additionalStars = 0;
        this.dontLosePp = false;
        this.doublePp = false;
        this.canPickFromTop = false;

        this.order = order;
        this.offer = offer;
        this.totem = totem;

        this.game = null;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getName() {
        return name;
    }

    public int getPp() {
        return pp;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    /**
     * Adds the specified amount of prestige points. Negative parameters are allowed.
     * @param pp number of prestige points to be added (or subtracted if negative)
     */
    public void addPp(int pp) {
        this.pp += pp;
    }

    public int getFood() {
        return food;
    }

    /**
     * Sets the player's food to the specified amount. Does not allow to set food to a negative number.
     * @param food the new food amount. Cannot be negative.
     * @throws IllegalArgumentException if the specified amount of food is negative
     */
    public void setFood(int food) throws IllegalArgumentException {
        if (food < 0) {
            throw new IllegalArgumentException("trying to set food to a negative value");
        }
        this.food = food;
    }

    /**
     * Adds the specified amount of food. Negative parameters are allowed.
     * @param food amount of food to be added (or subtracted if negative)
     * @throws IllegalArgumentException if the resulting food would become negative
     */
    public void addFood(int food) throws IllegalArgumentException {
        if (this.food + food < 0) {
            throw new IllegalArgumentException("trying to set food to a negative value");
        }
        this.food += food;
    }

    /**
     * Adds the specified {@code Card} to the player's tribe, throwing exceptions in case of {@code Event} cards or unaffordable {@code Buildings}.
     * @param card the card to be added to the player's tribe
     * @throws IllegalActionException if the given card is an {@code Event} or a {@code Building} the player cannot afford
     */
    public void addCard(Card card) throws IllegalActionException {
        card.addToPlayer(this);
    }

    /**
     * Adds the specified card to the player's list of {@code Artists}.
     * @param artist the card to be added to the player's tribe
     */
    public void addArtist(Artist artist) {
        this.artists.add(artist);
    }

    /**
     * Adds the specified card to the player's list of {@code Gatherers}.
     * @param gatherer the card to be added to the player's tribe
     */
    public void addGatherer(Gatherer gatherer) {
        this.gatherers.add(gatherer);
    }

    /**
     * Adds the specified card to the player's list of {@code Hunters}, calculating and adding the bonus of food it possibly provides.
     * @param hunter the card to be added to the player's tribe
     */
    public void addHunter(Hunter hunter) {
        try{
            this.hunters.add(hunter);
        }catch(Exception e){
            e.printStackTrace();
        }
        if (hunter.getIcon()) {
            addFood(getNumHunters());
        }
    }

    /**
     * Adds the specified card to the player's list of {@code Inventors}.
     * @param inventor the card to be added to the player's tribe
     */
    public void addInventor(Inventor inventor) {
        this.inventors.add(inventor);
    }

    /**
     * Adds the specified card to the player's list of {@code Shamans}.
     * @param shaman the card to be added to the player's tribe
     */
    public void addShaman(Shaman shaman) {
        this.shamans.add(shaman);
    }

    /**
     * Adds the specified card to the player's list of {@code Builders}.
     * @param builder the card to be added to the player's tribe
     */
    public void addBuilder(Builder builder) {
        this.builders.add(builder);
    }

    /**
     * Counts the number of complete sets of {@code Character} cards in the tribe. A set is composed of one card for each character type.
     * @return the number of sets made of six different {@code Character} cards in the player's tribe
     */
    public int countSets() {
        int[] numCharacters = {getNumBuilders(), getNumHunters(), getNumShamans(), getNumInventors(), getNumGatherers(), getNumArtists()};
        return Arrays.stream(numCharacters).min().getAsInt();
    }

    /**
     * Adds the specified card to the player's list of {@code Buildings}.
     * @param building the card to be added to the player's tribe
     */
    public void addBuilding(Building building) {
        this.buildings.add(building);
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public List<Gatherer> getGatherers() {
        return gatherers;
    }

    public List<Hunter> getHunters() {
        return hunters;
    }

    public List<Inventor> getInventors() {
        return inventors;
    }

    public List<Shaman> getShamans() {
        return shamans;
    }

    public List<Builder> getBuilders() {
        return builders;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    /**
     * Counts the total number of {@code Character} cards in the tribe.
     * @return the count of all {@code Character} cards in the player's tribe
     */
    public int countNumCharacters() {
        return getNumArtists() + getNumGatherers() + getNumHunters() + getNumShamans() + getNumInventors() + getNumBuilders();
    }

    /**
     * Counts the total number of {@code Building} cards in the tribe.
     * @return the count of {@code Buildings} owned by the player
     */
    public int countNumBuildings() {
        return buildings.size();
    }

    /**
     * Counts the number of {@code Artist} cards in the player's tribe.
     * @return the count of {@code Artists} owned by the player
     */
    public int getNumArtists() {
        return artists.size();
    }

    /**
     * Counts the number of {@code Gatherer} cards in the player's tribe.
     * @return the count of {@code Gatherers} owned by the player
     */
    public int getNumGatherers() {
        return gatherers.size();
    }

    /**
     * Counts the number of {@code Inventor} cards in the player's tribe.
     * @return the count of {@code Inventors} owned by the player
     */
    public int getNumInventors() {
        return inventors.size();
    }

    /**
     * Counts the number of {@code Hunter} cards in the player's tribe.
     * @return the count of {@code Hunters} owned by the player
     */
    public int getNumHunters() {
        return hunters.size();
    }

    /**
     * Counts the number of {@code Shaman} cards in the player's tribe.
     * @return the count of {@code Shamans} owned by the player
     */
    public int getNumShamans() {
        return shamans.size();
    }

    /**
     * Counts the number of {@code Builder} cards in the player's tribe.
     * @return the count of {@code Builders} owned by the player
     */
    public int getNumBuilders() {
        return builders.size();
    }

    /**
     * Counts the total number of stars provided by {@code Shamans} in the tribe.
     * @return the count of star icons provided by the {@code Shamans} owned by the player
     */
    public int getNumStars() {
        int count = 0;

        for (Shaman shaman : shamans) {
                count += shaman.getStars();
        }
        return count;
    }

    /**
     * Calculates the amount of prestige points provided by {@code Builders} in the tribe at the end of the game.
     * @return the count of prestige points provided by the {@code Builders} owned by the player
     */
    public int countBuildersPp() {
        int points = 0;
        for (Builder builder : builders) {
                points += builder.getPp();
        }
        return points;
    }

    /**
     * Returns total prestige points of the tribe, including both characters and buildings, without calculating any building effect.
     * @return the total amount of prestige points provided by {@code Characters} in the player's tribe
     */
    public int countTribePp() {
        int points = 0;
        int numIcons = 0;
        Icon currIcon = null;
        List<Icon> seenIcons = new ArrayList<>();
        boolean found = false;

        // 10 pps awarded every 2 artists in the tribe
        points += 10 * (this.getNumArtists() / 2);

        // Builders provide as many pps as indicated on their card
        points += this.countBuildersPp();

        // Inventors provide a number of pps equal to the number of Inventors in the tribe
        // multiplied by the number of different Invention icons
        for (Inventor inventor : inventors) {
                found = false;
                currIcon = inventor.getInventionIcon();

                for(Icon icon : seenIcons){
                    if (currIcon == icon){
                        found = true;
                        break;
                    }
                }

                if(!found){
                    seenIcons.add(currIcon);
                    numIcons++;
                }
            }

        points += numIcons * this.getNumInventors();

        // Buildings provide as many pps as indicated on their card
        for(Building building : buildings) {
            points += building.getPp();
        }

        return points;
    }

    public boolean getDontLosePp() {
        return dontLosePp;
    }

    public void setDontLosePp(boolean dontLosePp) {
        this.dontLosePp = dontLosePp;
    }

    public int getAdditionalStars() {
        return additionalStars;
    }

    public void setAdditionalStars(int additionalStars) {
        this.additionalStars = additionalStars;
    }

    public void addAdditionalStars(int additionalStars) {
        this.additionalStars += additionalStars;
    }

    public int getFoodDiscount() {
        return foodDiscount;
    }

    public void setFoodDiscount(int foodDiscount) {
        this.foodDiscount = foodDiscount;
    }

    public void addFoodDiscount(int foodDiscount) {
        this.foodDiscount += foodDiscount;
    }

    public boolean getDoublePp() {
        return doublePp;
    }

    public void setDoublePp(boolean doublePp) {
        this.doublePp = doublePp;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public char getOffer() {
        return offer;
    }

    public void setOffer(char offer) {
        this.offer = offer;
    }

    public Game getGame() {
        return game;
    }

    /**
     * Converts the current {@code Player} object into DTO format, also converting all their variables and the cards in their tribe.
     * @return the corresponding {@code PlayerDTO} object
     */
    public PlayerDTO toDTO(){
        return new PlayerDTO(getName(), getArtists().stream().map(Artist::toDTO).toList(),
                getGatherers().stream().map(Gatherer::toDTO).toList(),
                getHunters().stream().map(Hunter::toDTO).toList(),
                getInventors().stream().map(Inventor::toDTO).toList(),
                getShamans().stream().map(Shaman::toDTO).toList(),
                getBuilders().stream().map(Builder::toDTO).toList(),
                getBuildings().stream().map(Building::toDTO).toList(),
                getPp(), getFood(), getOrder(), getOffer(), getCanPickFromTop(), totem);
    }

    public void setCanPickFromTop(boolean canPickFromTop) {
        this.canPickFromTop = canPickFromTop;
    }

    public boolean getCanPickFromTop() {
        return canPickFromTop;
    }

    public Totem getTotem() {
        return totem;
    }

    public void setTotem(Totem totem) {
        this.totem = totem;
    }
}