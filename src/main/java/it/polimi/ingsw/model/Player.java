package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a player in the game.
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
     * Adds the specified amount of prestige points, negative parameters allowed.
     * @param pp number of prestige points to add (or subtract if negative)
     */
    public void addPp(int pp) {
        this.pp += pp;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) throws IllegalArgumentException {
        if (food < 0) {
            throw new IllegalArgumentException("trying to set food to a negative value");
        }
        this.food = food;
    }

    /**
     * Adds the specified amount of food, negative parameters allowed.
     * @param food amount of food to add (or subtract if negative)
     */
    public void addFood(int food) throws IllegalArgumentException {
        if (this.food + food < 0) {
            throw new IllegalArgumentException("trying to set food to a negative value");
        }
        this.food += food;
    }

    public void addCard(Card card) throws IllegalActionException {
        card.addToPlayer(this);
    }

    public void addArtist(Artist artist) {
        this.artists.add(artist);
    }

    public void addGatherer(Gatherer gatherer) {
        this.gatherers.add(gatherer);
    }

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

    public void addInventor(Inventor inventor) {
        this.inventors.add(inventor);
    }

    public void addShaman(Shaman shaman) {
        this.shamans.add(shaman);
    }

    public void addBuilder(Builder builder) {
        this.builders.add(builder);
    }

    /**
     * Counts the number of complete sets of six different character cards in the tribe.
     * @return int
     */
    public int countSets() {
        int[] numCharacters = {getNumBuilders(), getNumHunters(), getNumShamans(), getNumInventors(), getNumGatherers(), getNumArtists()};
        return Arrays.stream(numCharacters).min().getAsInt();
    }

    /**
     * Adds a new building card to the list of buildings in the tribe.
     * @param card new building to be added to the tribe
     */
    public void addBuilding(Building card) {
        this.buildings.add(card);
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
     * Counts the total number of character cards in the tribe.
     * @return int
     */
    public int countNumCharacters() {
        return getNumArtists() + getNumGatherers() + getNumHunters() + getNumShamans() + getNumInventors() + getNumBuilders();
    }

    /**
     * Counts the total number of building cards in the tribe.
     * @return int
     */
    public int countNumBuildings() {
        return buildings.size();
    }

    /**
     * Counts the number of artists in the tribe.
     * @return int
     */
    public int getNumArtists() {
        return artists.size();
    }

    /**
     * Counts the number of gatherers in the tribe.
     * @return int
     */
    public int getNumGatherers() {
        return gatherers.size();
    }

    /**
     * Counts the number of inventors in the tribe.
     * @return int
     */
    public int getNumInventors() {
        return inventors.size();
    }

    /**
     * Counts the number of hunters in the tribe.
     * @return int
     */
    public int getNumHunters() {
        return hunters.size();
    }

    /**
     * Counts the number of shamans in the tribe.
     * @return int
     */
    public int getNumShamans() {
        return shamans.size();
    }

    /**
     * Counts the number of builders in the tribe.
     * @return int
     */
    public int getNumBuilders() {
        return builders.size();
    }

    /**
     * Counts the total number of stars provided by shamans in the tribe.
     * @return int
     */
    public int getNumStars() {
        int count = 0;

        for (Shaman shaman : shamans) {
                count += shaman.getStars();
        }
        return count;
    }

    /**
     * Calculates the amount of prestige points provided by builders in the tribe at the end of the game.
     * @return int
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

        for (Inventor inventor : inventors) {
                found = false;
                currIcon = inventor.getInventionIcon();

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

        points += numIcons * this.getNumInventors();

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