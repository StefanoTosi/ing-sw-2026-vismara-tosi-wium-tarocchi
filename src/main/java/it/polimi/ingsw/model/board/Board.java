package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.Card;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.zip.DataFormatException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.effects.*;
import it.polimi.ingsw.model.events.*;

/**
 * Game board, holds information about all elements present on the board
 */
public class Board {
    private List<Card> topRowTribe;
    private List<Building> topRowBuilding;
    private List<Card> bottomRowTribe;
    private List<Building> bottomRowBuilding;

    private Deck deckTribe;
    private Deck deckE1Building;
    private Deck deckE2Building;
    private Deck deckE3Building;

    private Order order;
    private List<Offer> offerPath;

    private Random rng;

    /**
     * Generates an empty board. Decks will be filled by initialize() and the rows will be arranged in the RefillBoardState
     */
    public Board(Random rng) {
        this.topRowTribe = new ArrayList<>();
        this.topRowBuilding = new ArrayList<>();
        this.bottomRowTribe = new ArrayList<>();
        this.bottomRowBuilding = new ArrayList<>();

        this.deckTribe = null;
        this.deckE1Building = null;
        this.deckE2Building = null;
        this.deckE3Building = null;

        this.order = null;
        this.offerPath = null;

        this.rng = rng;
    }

    /**
     * Initializes a starting board given the number of players
     */
    public void initialize(int numPlayers) throws IllegalArgumentException {
        if (numPlayers < 2 || numPlayers > 5) {
            throw new IllegalArgumentException("'numPlayers' must be in the range 2 - 5");
        }

        // Create decks
        List<Card> tribeI = new ArrayList<>();
        List<Card> tribeII = new ArrayList<>();
        List<Card> tribeIII = new ArrayList<>();

        List<Card> e1Building = new ArrayList<>();
        List<Card> e2Building = new ArrayList<>();
        List<Card> e3Building = new ArrayList<>();

        List<Card> finalEvents = new ArrayList<>();

        // Parse cards.json to load cards
        try {
            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(new File("src/main/resources/it/polimi/ingsw/cards.json"));
            JsonNode cardsNode = root.get("cards");

            for (JsonNode node : cardsNode) {
                Era era = Era.valueOf(node.get("era").asText());
                switch (node.get("role").asText()) {
                    case "Character":
                        Character character;
                        if (node.get("minNumPlayers").asInt() <= numPlayers) {
                            switch (node.get("type").asText()) {
                                case "Artist":
                                    character = new Artist(era);
                                    break;
                                case "Builder":
                                    character = new Builder(node.get("discount").asInt(), node.get("pp").asInt(), era);
                                    break;
                                case "Gatherer":
                                    character = new Gatherer(era);
                                    break;
                                case "Hunter":
                                    character = new Hunter(node.get("foodIcon").asBoolean(), era);
                                    break;
                                case "Inventor":
                                    character = new Inventor(Icon.valueOf((node.get("icon").asText().toUpperCase())), era);
                                    break;
                                case "Shaman":
                                    character = new Shaman(node.get("stars").asInt(), era);
                                    break;
                                default:
                                    throw new DataFormatException("Unrecognized character '" + node.get("type").asText() + "' while parsing cards.json");
                            }

                            // Insert character in the correct tribe deck
                            switch (era) {
                                case I:
                                    tribeI.add(character);
                                    break;
                                case II:
                                    tribeII.add(character);
                                    break;
                                case III:
                                    tribeIII.add(character);
                                    break;
                            }
                        }
                        break;

                    case "Event":
                        Event event;
                        switch (node.get("type").asText()) {
                            case "CavePaintings":
                                event = new CavePaintings(node.get("minNumArtists").asInt(), era, node.get("topPp").asInt(), node.get("bottomPp").asInt());
                                break;
                            case "ShamanicRitual":
                                event = new ShamanicRitual(node.get("winnerPp").asInt(), node.get("loserPp").asInt(), era);
                                break;
                            case "Hunt":
                                event = new Hunt(node.get("pp").asInt(), era);
                                break;
                            case "Sustenance":
                                event = new Sustenance(node.get("pp").asInt(), Era.valueOf(node.get("era").asText()));
                                break;
                            default:
                                throw new DataFormatException("Unrecognized event '" + node.get("type").asText() + "' while parsing cards.json");
                        }

                        if (node.get("final").asBoolean()) {
                            finalEvents.add(event);
                        } else {
                            switch (era) {
                                case I:
                                    tribeI.add(event);
                                    break;
                                case II:
                                    tribeII.add(event);
                                    break;
                                case III:
                                    tribeIII.add(event);
                                    break;
                            }
                        }

                        break;
                    case "Building":
                        //manca il passaggio di effect nel costruttore di building
                        Effect effect = Effect.valueOf(node.get("effect").get("effectId").asText());
                        int effectPp = 0;
                        Function<Player, Integer> getNumCharacter = (p) -> 0;
                        /* switch (effect) {
                            case D1, D2:
                                break;
                            case ES1, ESC1, ESC2, ESC3, EH, ECP:
                                break;
                            case ET1:
                                break;
                            case ET2:
                                break;
                            case EG1, EG2, EG4:
                                break;
                            case EG3:
                                effectPp = node.get("effect").get("pp").asInt();
                                break;
                            default:
                                throw new DataFormatException("Unrecognized effect Id '" + node.get("effect").get("effectId").asText() + "' while parsing cards.json");
                        }*/

                        // Get pp bonus when present
                        if (node.get("effect").get("pp") != null) {
                            effectPp = node.get("effect").get("pp").asInt();
                        }

                        // Get character when present
                        if (node.get("effect").get("character") != null) {
                            switch (node.get("effect").get("character").asText()) {
                                case "Artist":
                                    getNumCharacter = (p) -> p.getNumArtists();
                                    break;
                                case "Builder":
                                    getNumCharacter = (p) -> p.getNumBuilders();
                                    break;
                                case "Gatherer":
                                    getNumCharacter = (p) -> p.getNumGatherers();
                                    break;
                                case "Hunter":
                                    getNumCharacter = (p) -> p.getNumHunters();
                                    break;
                                case "Inventor":
                                    getNumCharacter = (p) -> p.getNumInventors();
                                    break;
                                case "Shaman":
                                    getNumCharacter = (p) -> p.getNumShamans();
                                    break;
                                default:
                                    throw new DataFormatException("Unrecognized character '" + node.get("type").asText() + "' while parsing cards.json");
                            }
                        }

                        Building building = new Building(Era.valueOf(node.get("era").asText()), node.get("cost").asInt(), node.get("pp").asInt(), effectPp, getNumCharacter, effect);

                        switch (era) {
                            case I:
                                e1Building.add(building);
                                break;
                            case II:
                                e2Building.add(building);
                                break;
                            case III:
                                e3Building.add(building);
                                break;
                        }
                        break;
                    default:
                        throw new DataFormatException("Unrecognized role '" + node.get("role").asText() + "' while parsing cards.json");
                }
            }
        } catch (Exception e) {
            System.out.println("Error in loading cards from json file");
            e.printStackTrace();
        }

        Deck deckTribeI = new Deck(tribeI, rng);
        Deck deckTribeII = new Deck(tribeII, rng);
        Deck deckTribeIII = new Deck(tribeIII, rng);

        Deck deckE1Building = new Deck(e1Building, rng);
        Deck deckE2Building = new Deck(e2Building, rng);
        Deck deckE3Building = new Deck(e3Building, rng);

        Deck deckFinalEvents = new Deck(finalEvents, rng);

        // Shuffle
        deckTribeI.shuffle();
        deckTribeII.shuffle();
        deckTribeIII.shuffle();

        deckE1Building.shuffle();
        deckE2Building.shuffle();
        deckE3Building.shuffle();

        deckFinalEvents.shuffle();

        // Remove cards from building decks
        int lim1 = 0;
        int lim2 = 0;
        int lim3 = 0;

        switch (numPlayers) {
            case 2:
                lim1 = deckE1Building.size() - 1;
                lim2 = deckE2Building.size() - 2;
                lim3 = deckE3Building.size() - 3;
                break;

            case 3:
                lim1 = deckE1Building.size() - 2;
                lim2 = deckE2Building.size() - 2;
                lim3 = deckE3Building.size() - 4;
                break;

            case 4:
                lim1 = deckE1Building.size() - 2;
                lim2 = deckE2Building.size() - 3;
                lim3 = deckE3Building.size() - 4;
                break;

            case 5:
                lim1 = deckE1Building.size() - 2;
                lim2 = deckE2Building.size() - 3;
                lim3 = deckE3Building.size() - 5;
                break;
        }

        for (int i = 0; i < lim1; i++) {
            deckE1Building.draw();
        }
        for (int i = 0; i < lim2; i++) {
            deckE2Building.draw();
        }
        for (int i = 0; i < lim3; i++) {
            deckE3Building.draw();
        }

        // Stack tribe deck
        this.deckTribe = deckFinalEvents.stack(deckTribeIII.stack(deckTribeII.stack(deckTribeI)));
        this.deckE1Building = deckE1Building;
        this.deckE2Building = deckE2Building;
        this.deckE3Building = deckE3Building;

        // Parse tiles.json to load tiles
        this.offerPath = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(new File("src/main/resources/it/polimi/ingsw/tiles.json"));
            JsonNode tilesNode = root.get("tiles");

            for (JsonNode node : tilesNode) {
                switch (node.get("type").asText()) {
                    case "Order":
                        if (node.get("numPlayers").asInt() == numPlayers) {
                            List<Integer> ppBonus = new ArrayList<>();
                            for (JsonNode pp : node.get("ppBonus")) {
                                ppBonus.add(pp.asInt());
                            }

                            List<Integer> foodBonus = new ArrayList<>();
                            for (JsonNode food : node.get("foodBonus")) {
                                foodBonus.add(food.asInt());
                            }

                            this.order = new Order(numPlayers, ppBonus, foodBonus);
                        }
                        break;
                    case "Offer":
                        if (node.get("minNumPlayers").asInt() <= numPlayers) {
                            this.offerPath.add(new Offer(
                                    node.get("order").asText().charAt(0),
                                    node.get("foodBonus").asInt(),
                                    node.get("drawTop").asInt(),
                                    node.get("drawBottom").asInt()
                            ));
                        }
                        break;
                    default:
                        throw new DataFormatException("Unrecognized type '" + node.get("type").asText() + "' while parsing tiles.json");
                }
            }
        } catch (Exception e) {
            System.out.println("Error in loading tiles from json file");
            e.printStackTrace();
        }

        // Filling top and bottom rows is done in a GameState
    }

    public List<Card> getTopRowTribe() {
        return topRowTribe;
    }

    public List<Building> getTopRowBuilding() {
        return topRowBuilding;
    }

    public List<Card> getBottomRowTribe() {
        return bottomRowTribe;
    }

    public List<Building> getBottomRowBuilding() {
        return bottomRowBuilding;
    }

    public void setTopRowTribe(List<Card> topRowTribe) {
        this.topRowTribe = topRowTribe;
    }

    public void setTopRowBuilding(List<Building> topRowBuilding) {
        this.topRowBuilding = topRowBuilding;
    }

    public void setBottomRowTribe(List<Card> bottomRowTribe) {
        this.bottomRowTribe = bottomRowTribe;
    }

    public void setBottomRowBuilding(List<Building> bottomRowBuilding) {
        this.bottomRowBuilding = bottomRowBuilding;
    }

    /**
     * Picks the card in the top tribe row at position pos
     */
    public Card drawFromTopRowTribe(int pos) throws IllegalArgumentException {
        if (pos < 0 || pos >= topRowTribe.size()) {
            throw new IllegalArgumentException("'pos' is not a valid index");
        }
        return topRowTribe.remove(pos);
    }

    /**
     * Picks the card in the top building row at position pos
     */
    public Building drawFromTopRowBuilding(int pos) throws IllegalArgumentException {
        if (pos < 0 || pos >= topRowBuilding.size()) {
            throw new IllegalArgumentException("'pos' is not a valid index");
        }
        return topRowBuilding.remove(pos);
    }

    /**
     * Picks the card in the bottom tribe row at position pos
     */
    public Card drawFromBottomRowTribe(int pos) throws IllegalArgumentException {
        if (pos < 0 || pos >= bottomRowTribe.size()) {
            throw new IllegalArgumentException("'pos' is not a valid index");
        }
        return bottomRowTribe.remove(pos);
    }

    /**
     * Picks the card in the bottom building row at position pos
     */
    public Building drawFromBottomRowBuilding(int pos) throws IllegalArgumentException {
        if (pos < 0 || pos >= bottomRowBuilding.size()) {
            throw new IllegalArgumentException("'pos' is not a valid index");
        }
        return bottomRowBuilding.remove(pos);
    }

    public Deck getDeckTribe() {
        return deckTribe;
    }

    public Deck getDeckE1Building() {
        return deckE1Building;
    }

    public Deck getDeckE2Building() {
        return deckE2Building;
    }

    public Deck getDeckE3Building() {
        return deckE3Building;
    }

    public Order getOrder() {
        return order;
    }

    public List<Offer> getOfferPath() {
        return offerPath;
    }

    public BoardDTO toDTO(){
        return new BoardDTO(getTopRowTribe().size(),
                getTopRowTribe().stream().map(Card::cardToDTO).toList(),
                getTopRowBuilding().stream().map(Building::toDTO).toList(),
                getBottomRowTribe().size(),
                getBottomRowTribe().stream().map(Card::cardToDTO).toList(),
                getBottomRowBuilding().stream().map(Building::toDTO).toList(),
                (getDeckTribe() != null)? getDeckTribe().size() : 0,
                (getDeckE1Building() != null)?getDeckE1Building().size():0,
                (getDeckE2Building() != null)?getDeckE2Building().size():0,
                (getDeckE3Building() != null)?getDeckE3Building().size():0,
                (getOrder() != null)?getOrder().toDTO():null,
                (getOfferPath() != null)?getOfferPath().stream().map(Offer::toDTO).toList():null);
    }
}
