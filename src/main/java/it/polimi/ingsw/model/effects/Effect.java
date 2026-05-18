package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.Inventor;
import it.polimi.ingsw.model.characters.Character;

public enum Effect {
    /**
     * On draw:
     * Starting from when you have this Building, every time you complete a set of 6 different
     * Character cards, you take 5 Food tokens. You do not receive Food tokens for sets
     * already completed at the time of acquiring the Building.
     */
    D1 {
        @Override
        public void applyEffectDraw (Player player, int numSets, Character character) {
            if (player.countSets() != numSets) {
                player.addFood(5);
            }
        }
    },

    /** Event sustenance:
     * During the Sustenance Event, you have a discount of 1 Food token on the total you would have to pay,
     * for each of the indicated Characters in your tribe (Artists/Inventors/Gatherers).
     */
    ES1 {
        @Override
        public void applyEffectEventSustenance(Player player, Building building) {
            player.setFoodDiscount(player.getFoodDiscount() + building.getNumCharacter.apply(player));
        }
    },

    /**
     * Event shamanic:
     * During the Shamanic Ritual Event, you do not lose Prestige Points if you have fewer star icons
     * than all other players.
     */
    ESC1 {
        @Override
        public void applyEffectEventShamanicRitual (Player player, Building building) {
            player.setDontLosePp(true);
        }
    },

    /**
     * Tile bonus:
     * If at the end of your turn (also during the last round), when you move your Totem back to
     * the Turn Order tile, you place it in a space that provides a bonus in Food, you immediately
     * take 1 additional Food token. If you place the Totem in the last space, you pay 1 Food token
     * normally, and the building has no effect.
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
     * On draw:
     * Starting from when you have this Building, every time you obtain a pair of identical
     * Inventors (with the same Invention icon), you take 3 Food tokens. You do not take Food for
     * pairs already owned at the time of acquiring the Building
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
     * Event shamanic:
     * During the Shamanic Ritual Event, your tribe has 3 additional star icons
     */
    ESC2 {
        @Override
        public void whenDrawn (Player player) {
            player.addAdditionalStars(3);
        }
    },

    /**
     * Event shamanic:
     * During the Shamanic Ritual Event, if you have more  icons than any of the other players,
     * you gain double the indicated Prestige Points. You don't gain double Prestige Points in case of a tie.
     */
    ESC3 {
        @Override
        public void applyEffectEventShamanicRitual (Player player, Building building) {
            player.setDoublePp(true);
        }
    },

    /**
     * Event hunt:
     * During the Hunt Event, you take 1 Food token and gain 1 additional Prestige Point for each
     * Hunter in your tribe.
     */
    EH {
        @Override
        public void applyEffectEventHunt (Player player, Building building) {
            player.addFood(player.getNumHunters());
            player.addPp(player.getNumHunters());
        }
    },

    /**
     * End game:
     * At the end of the game, you gain double the Prestige Points indicated on the Builder cards
     * in your tribe.
     */
    EG1 {
        @Override
        public void applyEffectEndGame (Player player, Building building) {
            player.addPp(player.countBuildersPp());
        }
    },

    /**
     * Event cave painting:
     * During the Cave Paintings Event, you take 1 Food token for each Artist in your tribe.
     */
    ECP {
        @Override
        public void applyEffectEventCavePaintings (Player player, Building building) {
            player.addFood(player.getNumArtists());
        }
    },
    /**
     * End game:
     * At the end of the game, you gain 6 Prestige Points for each set of 6 different Character
     * cards in your tribe
     */
    EG2 {
        @Override
        public void applyEffectEndGame (Player player, Building building) {
            player.addPp(6 * player.countSets());
        }
    },

    /**
     * End game:
     * At the end of the game, you gain the indicated amount of Prestige Points for each Character
     * card of the indicated type in your tribe.
     */
    EG3 {
        @Override
        public void applyEffectEndGame (Player player, Building building) {
            player.addPp(building.getEffectPp() * building.getNumCharacter.apply(player));
        }
    },

    /**
     * End turn:
     * After resolving all actions (once all Totems have been moved back to the Turn Order tile)
     * and before the End of the Round phase, you can take 1 Character or 1 Building card (paying
     * its cost) from the top row
     */
    ET2 {
        @Override
        public void applyEffectEndTurn (Player player) {
            player.setCanPickFromTop(true);
        }
    },

    /**
     * End game:
     * At the end of the game, you gain 25 Prestige Points.
     */
    EG4 {
        @Override
        public void applyEffectEndGame (Player player, Building building) {
            player.addPp(building.getEffectPp());
        }
    };

    public void applyEffectEventCavePaintings (Player player, Building building) {}
    public void applyEffectEventHunt (Player player, Building building) {}
    public void applyEffectEventShamanicRitual (Player player, Building building) {}
    public void applyEffectEventSustenance (Player player, Building building) {}

    public void applyEffectDraw (Player player, int numSets, Character character) {}
    public void applyEffectEndGame (Player player, Building building) {}
    public void applyEffectEndTurn (Player player) {}
    public void applyEffectTileBonus (Player player, Building building) {}

    public void whenDrawn (Player player) {}
}
