package it.polimi.ingsw.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.characters.DTO.*;
import it.polimi.ingsw.model.effects.BuildingDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds information about a {@code Player} in DTO format.
 */
public class PlayerDTO implements Serializable {
    private final String name;

    private final List<ArtistDTO> artists;
    private final List<GathererDTO> gatherers;
    private final List<HunterDTO> hunters;
    private final List<InventorDTO> inventors;
    private final List<ShamanDTO> shamans;
    private final List<BuilderDTO> builders;
    private final List<BuildingDTO> buildings;

    private final int pp;
    private final int food;
    private final int order;
    private final char offer;
    private final boolean canPickFromTop;
    private final Totem totem;

    /**
     * Generates and initializes a {@code PlayerDTO} object, filled with the specified parameters.
     * @param name the player's name
     * @param artists the list of {@code Artists} in the player's tribe, in DTO format
     * @param gatherers the list of {@code Gatherers} in the player's tribe, in DTO format
     * @param hunters the list of {@code Hunters} in the player's tribe, in DTO format
     * @param inventors the list of {@code Inventors} in the player's tribe, in DTO format
     * @param shamans the list of {@code Shamans} in the player's tribe, in DTO format
     * @param builders the list of {@code Builders} in the player's tribe, in DTO format
     * @param buildings the list of {@code Buildings} in the player's tribe, in DTO format
     * @param pp the amount of prestige points accumulated by the player
     * @param food the amount of food available to the player
     * @param order the player's current position on the {@code Order} tile
     * @param offer the player's chosen {@code Offer} tile
     * @param canPickFromTop {@code true} if the player is allowed to draw an additional card from the top row, thanks to a building effect, {@code false} otherwise
     * @param totem the player's chosen {@code Totem} color
     */
    @JsonCreator
    public PlayerDTO(@JsonProperty("name") String name, @JsonProperty("artists") List<ArtistDTO> artists, @JsonProperty("gatherers") List<GathererDTO> gatherers, @JsonProperty("hunters") List<HunterDTO> hunters,
                     @JsonProperty("inventors") List<InventorDTO> inventors, @JsonProperty("shamans") List<ShamanDTO> shamans, @JsonProperty("builders") List<BuilderDTO> builders,
                     @JsonProperty("buildings") List<BuildingDTO> buildings, @JsonProperty("pp") int pp, @JsonProperty("food") int food, @JsonProperty("order") int order, @JsonProperty("offer") char offer,
                     @JsonProperty("canPickFromTop") boolean canPickFromTop, @JsonProperty("totem") Totem totem) {
        this.name = name;
        this.artists = List.copyOf(artists);
        this.gatherers = List.copyOf(gatherers);
        this.hunters = List.copyOf(hunters);
        this.inventors = List.copyOf(inventors);
        this.shamans = List.copyOf(shamans);
        this.builders = List.copyOf(builders);
        this.buildings = List.copyOf(buildings);
        this.pp = pp;
        this.food = food;
        this.order = order;
        this.offer = offer;
        this.canPickFromTop = canPickFromTop;
        this.totem = totem;
    }

    /**
     * Converts the current {@code PlayerDTO} object into the corresponding {@code Player},
     * converting all of its components as well through calls to other {@code fromDTO} methods.
     * @return the {@code Player} obtained by converting all its variables from DTO to standard objects
     */
    public Player fromDTO() {
        return new Player(
                this.name,
                this.artists.stream()
                        .map(ArtistDTO::fromDTO)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.gatherers.stream()
                        .map(GathererDTO::fromDTO)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.hunters.stream()
                        .map(HunterDTO::fromDTO)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.inventors.stream()
                        .map(InventorDTO::fromDTO)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.shamans.stream()
                        .map(ShamanDTO::fromDTO)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.builders.stream()
                        .map(BuilderDTO::fromDTO)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.buildings.stream()
                        .map(BuildingDTO::fromDTO)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.pp,
                this.food,
                this.order,
                this.offer,
                this.totem
        );
    }

    public String getName() {
        return name;
    }

    public List<ArtistDTO> getArtists() {
        return artists;
    }

    public List<GathererDTO> getGatherers() {
        return gatherers;
    }

    public List<HunterDTO> getHunters() {
        return hunters;
    }

    public List<InventorDTO> getInventors() {
        return inventors;
    }

    public List<ShamanDTO> getShamans() {
        return shamans;
    }

    public List<BuilderDTO> getBuilders() {
        return builders;
    }

    public List<BuildingDTO> getBuildings() {
        return buildings;
    }

    public int getPp() {
        return pp;
    }

    public int getFood() {
        return food;
    }

    public int getOrder() {
        return order;
    }

    public char getOffer() {
        return offer;
    }

    public boolean getCanPickFromTop() {
        return canPickFromTop;
    }

    public Totem getTotem() {
        return totem;
    }
}
