package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.Inventor;
import it.polimi.ingsw.model.characters.Character;

/**
 * Enumerates all possible {@code Building} effects.
 * <p>
 * Each {@code Effect} can allow the player special actions or grant them additional resources in different phases of the game.
 */
public enum Effect {
    /**
     * Starting from when they draw this {@code Building}, every time a player completes a set of 6 different
     * {@code Character} cards, they receive 5 food tokens. Players do not receive food tokens for sets
     * already completed at the time of acquiring the {@code Building}.
     */
    D1 {
        @Override
        public void applyEffectDraw (Player player, int numSets, Character character) {
            if (player.countSets() != numSets) {
                player.addFood(5);
            }
        }
    },

    /**
     * During the {@code Sustenance} event, the player has a discount of 1 food token on the total they would have to pay,
     * for each of the indicated {@code Character} in they tribe ({@code Artists}/{@code Inventors}/{@code Gatherers}).
     */
    ES1 {
        @Override
        public void applyEffectEventSustenance(Player player, Building building) {
            player.setFoodDiscount(player.getFoodDiscount() + building.getNumCharacter.apply(player));
        }
    },

    /**
     * During the {@code ShamanicRitual} event, the player does not lose prestige points if they have fewer star icons
     * than all other players.
     */
    ESC1 {
        @Override
        public void applyEffectEventShamanicRitual (Player player, Building building) {
            player.setDontLosePp(true);
        }
    },

    /**
     * If at the end of their turn (also during the last round), when the player moves their {@code Totem} back to
     * the {@code Order} tile, they place it in a space that provides a bonus in food, they immediately
     * take 1 additional food token. If the player places the {@code Totem} in the last space, they pay 1 food token
     * normally and the building has no effect.
     */
    ET1 {
        // Must be executed after players have moved back to the order tile
        @Override
        public void applyEffectTileBonus (Player player, Building building) {
            if (player.getGame().getBoard().getOrder().getFoodBonus(player.getOrder()) > 0) {
                player.addFood(1);
            }
        }
    },

    /**
     * Starting from when they draw the {@code Building}, every time the player obtains a pair of identical
     * {@code Inventors} (with the same Invention icon), they take 3 food tokens. The player does not take food for
     * pairs already owned at the time of acquiring the {@code Building}.
     */
    D2 {
        @Override
        public void applyEffectDraw (Player player, int numSets, Character character) {
            if (character.getName().equals("Inventor")) {
                Inventor inventor = (Inventor) character;
                for (Inventor i : player.getInventors()){
                    if (i.getInventionIcon().equals(inventor.getInventionIcon()) && !i.equals(inventor)) {
                        player.addFood(3);
                    }
                }
            }
        }
    },

    /**
     * During the {@code ShamanicRitual} event, the player's tribe has 3 additional star icons.
     */
    ESC2 {
        @Override
        public void whenDrawn (Player player) {
            player.addAdditionalStars(3);
        }
    },

    /**
     * During the {@code ShamanicRitual} event, if the player has more icons than any of the other players,
     * they gain double the indicated prestige points. They don't gain double prestige points in case of a tie.
     */
    ESC3 {
        @Override
        public void applyEffectEventShamanicRitual (Player player, Building building) {
            player.setDoublePp(true);
        }
    },

    /**
     * During the {@code Hunt} event, the player takes 1 food token and gains 1 additional prestige point for each
     * {@code Hunter} in their tribe.
     */
    EH {
        @Override
        public void applyEffectEventHunt (Player player, Building building) {
            player.addFood(player.getNumHunters());
            player.addPp(player.getNumHunters());
        }
    },

    /**
     * At the end of the game, the player gains double the prestige points indicated on the {@code Builder} cards in their tribe.
     */
    EG1 {
        @Override
        public void applyEffectEndGame (Player player, Building building) {
            player.addPp(player.countBuildersPp());
        }
    },

    /**
     * During the {@code CavePaintings} event, the player takes 1 food token for each {@code Artist} in their tribe.
     */
    ECP {
        @Override
        public void applyEffectEventCavePaintings (Player player, Building building) {
            player.addFood(player.getNumArtists());
        }
    },
    /**
     * At the end of the game, the player gains 6 prestige points for each set of 6 different {@code Character} cards in their tribe.
     */
    EG2 {
        @Override
        public void applyEffectEndGame (Player player, Building building) {
            player.addPp(6 * player.countSets());
        }
    },

    /**
     * At the end of the game, the player gains the indicated amount of prestige points for each {@code Character} card of the indicated type in their tribe.
     */
    EG3 {
        @Override
        public void applyEffectEndGame (Player player, Building building) {
            player.addPp(building.getEffectPp() * building.getNumCharacter.apply(player));
        }
    },

    /**
     * After resolving all actions (once all {@code Totems} have been moved back to the {@code Order} tile),
     * the player can take 1 additional card (paying its cost in case of a {@code Building}) from the top row.
     */
    ET2 {
        @Override
        public void applyEffectEndTurn (Player player) {
            player.setCanPickFromTop(true);
        }
    },

    /**
     * At the end of the game, the player gains 25 additional prestige p oints.
     */
    EG4 {
        @Override
        public void applyEffectEndGame (Player player, Building building) {
            player.addPp(building.getEffectPp());
        }
    };

    /**
     * Executes applicable {@code Building} effects during the {@code CavePaintings} event.
     * @param player the {@code Player} involved in the event
     * @param building the {@code Building} card in the player's tribe
     * @see Effect#ECP
     */
    public void applyEffectEventCavePaintings (Player player, Building building) {}

    /**
     * Executes applicable {@code Building} effects during the {@code Hunt} event.
     * @param player the {@code Player} involved in the event
     * @param building the {@code Building} card in the player's tribe
     * @see Effect#EH
     */
    public void applyEffectEventHunt (Player player, Building building) {}

    /**
     * Executes applicable {@code Building} effects during the {@code ShamanicRitual} event.
     * @param player the {@code Player} involved in the event
     * @param building the {@code Building} card in the player's tribe
     * @see Effect#ESC1
     * @see Effect#ESC3
     */
    public void applyEffectEventShamanicRitual (Player player, Building building) {}

    /**
     * Executes applicable {@code Building} effects during the {@code Sustenance} event.
     * @param player the {@code Player} involved in the event
     * @param building the {@code Building} card in the player's tribe
     * @see Effect#ES1
     */
    public void applyEffectEventSustenance (Player player, Building building) {}

    /**
     * Executes applicable {@code Building} effects whenever a {@code Character} card is being drawn.
     * @param player the {@code Player} drawing the card
     * @param numSets the number of sets owned by the player before drawing the card
     * @param character the {@code Card} being drawn by the player
     * @see Effect#D1
     * @see Effect#D2
     */
    public void applyEffectDraw (Player player, int numSets, Character character) {}

    /**
     * Executes applicable {@code Building} effects at the end of the game.
     * @param player the current {@code Player}
     * @param building the {@code Building} card in the player's tribe
     * @see Effect#EG1
     * @see Effect#EG2
     * @see Effect#EG3
     * @see Effect#EG4
     */
    public void applyEffectEndGame (Player player, Building building) {}

    /**
     * Executes applicable {@code Building} effects at the end of a round.
     * @param player the current {@code Player}
     * @see Effect#ET2
     */
    public void applyEffectEndTurn (Player player) {}

    /**
     * Executes applicable {@code Building} effects when a player's {@code Totem} is moved back to the {@code Order} tile.
     * @param player the current {@code Player}
     * @param building the {@code Building} card in the player's tribe
     * @see Effect#ET1
     */
    public void applyEffectTileBonus (Player player, Building building) {}

    /**
     * Executes applicable effects when a {@code Building} is drawn.
     * @param player the {@code Player} drawing the card
     * @see Effect#ESC2
     */
    public void whenDrawn (Player player) {}
}