package it.polimi.ingsw.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.characters.DTO.*;
import it.polimi.ingsw.model.effects.BuildingDTO;

import java.io.Serializable;
import java.util.List;

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

    public Player fromDTO() {
        return new Player(this.name, this.artists.stream().map(ArtistDTO::fromDTO).toList(),
                this.gatherers.stream().map(GathererDTO::fromDTO).toList(),
                this.hunters.stream().map(HunterDTO::fromDTO).toList(),
                this.inventors.stream().map(InventorDTO::fromDTO).toList(),
                this.shamans.stream().map(ShamanDTO::fromDTO).toList(),
                this.builders.stream().map(BuilderDTO::fromDTO).toList(),
                this.buildings.stream().map(BuildingDTO::fromDTO).toList(),
                this.pp, this.food, this.order, this.offer, this.totem);
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
