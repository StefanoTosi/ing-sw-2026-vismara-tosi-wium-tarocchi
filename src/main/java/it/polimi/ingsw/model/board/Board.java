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
 * Holds information about all elements present on the board.<br>
 * It contains a covered deck, a top and a bottom row of cards, an order tile and an offer path.
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
     * Generates an empty board. Decks will be filled by initialize() and the rows will be arranged in <code>RefillBoardState</code>.
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
     * Generates a board filled with the specified parameters.
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
    public Board(List<Card> topRowTribe, List<Building> topRowBuilding, List<Card> bottomRowTribe, List<Building> bottomRowBuilding,
                 Deck deckTribe, Deck deckE1Building, Deck deckE2Building, Deck deckE3Building, Order order, List<Offer> offerPath) {
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

    /**
     * Initializes a starting board, given the number of players.<br>
     * It parses the json file containing the cards, creating the covered decks and the offer path.
     * The top and bottom rows are left empty as they will be filled in <code>RefillBoardState</code>.
     * @param numPlayers the number of players in the current game
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

            int id = 1;
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
                            character.setId(id);
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
                        event.setId(id);

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
                        Effect effect = Effect.valueOf(node.get("effect").get("effectId").asText());
                        int effectPp = 0;
                        Function<Player, Integer> getNumCharacter = (p) -> 0;

                        // Get pp bonus when present
                        if (node.get("effect").get("pp") != null) {
                            effectPp = node.get("effect").get("pp").asInt();
                        }

                        // Get character when present
                        if (node.get("effect").get("character") != null) {
                            switch (node.get("effect").get("character").asText()) {
                                case "Artist":
                                    getNumCharacter = Player::getNumArtists;
                                    break;
                                case "Builder":
                                    getNumCharacter = Player::getNumBuilders;
                                    break;
                                case "Gatherer":
                                    getNumCharacter = Player::getNumGatherers;
                                    break;
                                case "Hunter":
                                    getNumCharacter = Player::getNumHunters;
                                    break;
                                case "Inventor":
                                    getNumCharacter = Player::getNumInventors;
                                    break;
                                case "Shaman":
                                    getNumCharacter = Player::getNumShamans;
                                    break;
                                default:
                                    throw new DataFormatException("Unrecognized character '" + node.get("type").asText() + "' while parsing cards.json");
                            }
                        }

                        Building building = new Building(Era.valueOf(node.get("era").asText()), node.get("cost").asInt(), node.get("pp").asInt(), effectPp, getNumCharacter, effect);
                        building.setId(id);

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
                id += 1;
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
     * Returns the card in the top tribe row at specified position, without removing it from the row.
     * @param pos the index of the card to be drawn
     * @return the card present in the specified position
     * @throws IllegalArgumentException if the specified position is not a valid index
     */
    public Card drawFromTopRowTribe(int pos) throws IllegalArgumentException {
        if (pos < 0 || pos >= topRowTribe.size()) {
            throw new IllegalArgumentException("'pos' is not a valid index");
        } else {
            return topRowTribe.get(pos);
        }
    }

    /**
     * Returns the card in the top building row at specified position, without removing it from the row.
     * @param pos the index of the card to be drawn
     * @return the card present in the specified position
     * @throws IllegalArgumentException if the specified position is not a valid index
     */
    public Building drawFromTopRowBuilding(int pos) throws IllegalArgumentException {
        if (pos < 0 || pos >= topRowBuilding.size()) {
            throw new IllegalArgumentException("'pos' is not a valid index");
        }
        return topRowBuilding.get(pos);
    }

    /**
     * Returns the card in the bottom tribe row at specified position, without removing it from the row.
     * @param pos the index of the card to be drawn
     * @return the card present in the specified position
     * @throws IllegalArgumentException if the specified position is not a valid index
     */
    public Card drawFromBottomRowTribe(int pos) throws IllegalArgumentException {
        if (pos < 0 || pos >= bottomRowTribe.size()) {
            throw new IllegalArgumentException("'pos' is not a valid index");
        } else {
            return bottomRowTribe.get(pos);
        }
    }

    /**
     * Returns the card in the bottom building row at specified position, without removing it from the row.
     * @param pos the index of the card to be drawn
     * @return the card present in the specified position
     * @throws IllegalArgumentException if the specified position is not a valid index
     */
    public Building drawFromBottomRowBuilding(int pos) throws IllegalArgumentException {
        if (pos < 0 || pos >= bottomRowBuilding.size()) {
            throw new IllegalArgumentException("'pos' is not a valid index");
        }
        return bottomRowBuilding.get(pos);
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

    /**
     * Calculates available cards the specified player is allowed to draw from the top row, subtracting events and unaffordable buildings.
     * @param player
     * @return the number of available cards
     */
    public int drawableCardsFromTop(Player player) {
        int topRowSize = topRowTribe.size() + topRowBuilding.size();

        // Subtract event cards
        //TODO: si può sostituire l'instance of?
        for (Card card : this.topRowTribe) {
            if (card instanceof Event) {
                topRowSize--;
            }
        }

        //Subtract unaffordable buildings
        for (Building building : this.topRowBuilding) {
            if (building.discountedCost(player) > player.getFood()) {
                topRowSize--;
            }
        }

        return topRowSize;
    }

    /**
     * Calculates available cards the specified player is allowed to draw from the bottom row, subtracting events and unaffordable buildings.
     * @param player
     * @return the number of available cards
     */
    public int drawableCardsFromBottom(Player player) {
        int bottomRowSize = bottomRowTribe.size() + bottomRowBuilding.size();

        // Subtract event cards
        //TODO: si può sostituire l'instance of?
        for(Card card : this.bottomRowTribe) {
            if(card instanceof Event) {
                bottomRowSize--;
            }
        }

        //Subtract unaffordable buildings
        for(Building building : this.bottomRowBuilding) {
            if(building.discountedCost(player) > player.getFood()) {
                bottomRowSize--;
            }
        }

        return bottomRowSize;
    }

    /**
     * Verifies if the player can draw at least one available card,
     * considering the offer tile where their totem is placed and the available cards on the board.
     * @param player
     * @param drawnTop number of cards the player has already drawn from the top row in the current turn
     * @param drawnBottom number of cards the player has already drawn from the bottom row in the current turn
     * @return {@code true} if the player can draw at least one card,
     *         {@code false} otherwise
     */
    public boolean playerCanDraw(Player player, int drawnTop, int drawnBottom) {
        int drawTop = 0;
        int drawBottom = 0;

        //Look for the player's drawTop and drawBottom numbers
        for(Offer o : this.offerPath) {
            if(o.getOrder() == player.getOffer()) {
                drawTop =  o.getDrawTop();
                drawBottom = o.getDrawBottom();
            }
        }

        drawTop -= drawnTop;
        drawBottom -= drawnBottom;

        if (drawTop > 0 && drawableCardsFromTop(player) > 0) {
            return true;
        }
        if (drawBottom > 0 && drawableCardsFromBottom(player) > 0) {
            return true;
        }

        return false;
    }

    /**
     * Converts the current board into the corresponding DTO class.
     * @return the corresponding board in DTO format
     */
    public BoardDTO toDTO(){
        return new BoardDTO(getTopRowTribe().stream().map(Card::toDTO).toList(),
                getTopRowBuilding().stream().map(Building::toDTO).toList(),
                getBottomRowTribe().stream().map(Card::toDTO).toList(),
                getBottomRowBuilding().stream().map(Building::toDTO).toList(),
                (getDeckTribe() != null) ? getDeckTribe().getDeck().stream().map(Card::toDTO).toList() : null,
                (getDeckE1Building() != null) ? getDeckE1Building().getDeck().stream().map(c -> (Building) c).map(Building::toDTO).toList() : null,
                (getDeckE2Building() != null) ? getDeckE2Building().getDeck().stream().map(c -> (Building) c).map(Building::toDTO).toList() : null,
                (getDeckE3Building() != null) ? getDeckE3Building().getDeck().stream().map(c -> (Building) c).map(Building::toDTO).toList() : null,
                (getOrder() != null) ? getOrder().toDTO() : null,
                (getOfferPath() != null) ? getOfferPath().stream().map(Offer::toDTO).toList() : null
        );
    }
}
