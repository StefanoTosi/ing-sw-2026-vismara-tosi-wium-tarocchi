package it.polimi.ingsw.model.board;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.CardDTO;
import it.polimi.ingsw.model.effects.BuildingDTO;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

/**
 * Holds information about the current {@code Board} in DTO format.
 */
public class BoardDTO implements Serializable {
    private List<CardDTO> topRowTribe;
    private List<BuildingDTO> topRowBuilding;
    private List<CardDTO> bottomRowTribe;
    private List<BuildingDTO> bottomRowBuilding;

    private List<CardDTO> deckTribe;
    private List<BuildingDTO> deckE1Building;
    private List<BuildingDTO> deckE2Building;
    private List<BuildingDTO> deckE3Building;

    private OrderDTO order;
    private List<OfferDTO> offerPath;

    @JsonCreator
    private BoardDTO() {}

    /**
     * Generates and initializes a {@code BoardDTO} object, filled with the specified parameters.
     * @param topRowTribe the top row of Character and Event cards
     * @param topRowBuilding the top row of Building cards
     * @param bottomRowTribe the bottom row of Character and Event cards
     * @param bottomRowBuilding the bottom row of Building cards
     * @param deckTribe the covered deck of Character and Event cards
     * @param deckE1Building the covered deck of first era buildings
     * @param deckE2Building the covered deck of second era buildings
     * @param deckE3Building the covered deck of third era buildings
     * @param order the tile where totems are placed before being moved to the offer path
     * @param offerPath the list of tiles where players can place their totem
     */
    public BoardDTO(List<CardDTO> topRowTribe, List<BuildingDTO> topRowBuilding, List<CardDTO> bottomRowTribe, List<BuildingDTO> bottomRowBuilding,
                    List<CardDTO> deckTribe, List<BuildingDTO> deckE1Building, List<BuildingDTO> deckE2Building, List<BuildingDTO> deckE3Building,
                    OrderDTO order, List<OfferDTO> offerPath) {
        this.topRowTribe = topRowTribe;
        this.topRowBuilding = topRowBuilding;
        this.bottomRowTribe = bottomRowTribe;
        this.bottomRowBuilding = bottomRowBuilding;
        this.deckTribe = deckTribe;
        this.deckE1Building = deckE1Building;
        this.deckE2Building = deckE2Building;
        this.deckE3Building = deckE3Building;
        this.order = order;
        this.offerPath = offerPath;
    }

    public List<CardDTO> getTopRowTribe() {
        return topRowTribe;
    }

    public List<BuildingDTO> getTopRowBuilding() {
        return topRowBuilding;
    }

    public List<CardDTO> getBottomRowTribe() {
        return bottomRowTribe;
    }

    public List<BuildingDTO> getBottomRowBuilding() {
        return bottomRowBuilding;
    }

    public List<CardDTO> getDeckTribe() {
        return deckTribe;
    }

    public List<BuildingDTO> getDeckE1Building() {
        return deckE1Building;
    }

    public List<BuildingDTO> getDeckE2Building() {
        return deckE2Building;
    }

    public List<BuildingDTO> getDeckE3Building() {
        return deckE3Building;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public List<OfferDTO> getOfferPath() {
        return offerPath;
    }

    /**
     * Converts the current {@code BoardDTO} object into the corresponding {@code Board},
     * converting all of its components as well through calls to other {@code fromDTO} methods.
     * @return the {@code Board} obtained by converting all its components from DTO to standard objects
     */
    public Board fromDTO() {
        return new Board(
                this.topRowTribe.stream()
                        .map(CardDTO::fromDTO)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.topRowBuilding.stream()
                        .map(BuildingDTO::fromDTO)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.bottomRowTribe.stream()
                        .map(CardDTO::fromDTO)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.bottomRowBuilding.stream()
                        .map(BuildingDTO::fromDTO)
                        .collect(Collectors.toCollection(ArrayList::new)),
                new Deck(
                        this.deckTribe.stream()
                                .map(CardDTO::fromDTO)
                                .collect(Collectors.toCollection(ArrayList::new)),
                        null
                ),
                new Deck(
                        this.deckE1Building.stream()
                                .map(CardDTO::fromDTO)
                                .collect(Collectors.toCollection(ArrayList::new)),
                        null
                ),
                new Deck(
                        this.deckE2Building.stream()
                                .map(CardDTO::fromDTO)
                                .collect(Collectors.toCollection(ArrayList::new)),
                        null
                ),
                new Deck(
                        this.deckE3Building.stream()
                                .map(CardDTO::fromDTO)
                                .collect(Collectors.toCollection(ArrayList::new)),
                        null
                ),
                (getOrder() != null)
                        ? getOrder().fromDTO()
                        : null,
                (getOfferPath() != null)
                        ? getOfferPath().stream()
                          .map(OfferDTO::fromDTO)
                          .collect(Collectors.toCollection(ArrayList::new))
                        : null
        );
    }
}