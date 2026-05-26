package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.events.DTO.ShamanicRitualDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * During this {@code Event}, the player with the most star icons, provided by {@code Shamans} and possibly {@code Building} effects in their tribe,
 * is awarded the specified amount of prestige points. The player with the least stars is deducted the specified amount of prestige points.
 */
public class ShamanicRitual extends Event {

    private final int winnerPp;
    private final int loserPp;

    /**
     * Generates a {@code ShamanicRitual} object, filled with the specified parameters.
     * @param winnerPp the amount of prestige points awarder to the event winners
     * @param loserPp the amount of prestige points deducted from the event losers. This parameter is required to be negative.
     * @param era the {@code Era} the card belongs to
     */
    public ShamanicRitual(int winnerPp, int loserPp, Era era){
        super(era);
        this.winnerPp = winnerPp;
        this.loserPp = loserPp;
        this.name = "ShamanicRitual";
    }

    public int getWinnerPp() {
        return winnerPp;
    }

    public int getLoserPp() {
        return loserPp;
    }

    /**
     * Resolves the {@code Event} effect. The player with the most stars wins the event, the player with the least stars loses.
     * @param players the list of {@code Players} involved in the {@code Event}
     * @return the list of {@code EventResults}, containing the food & pps deltas for each player
     */
    @Override
    public List<EventResult> applyEffect(List<Player> players){
        List<EventResult> results = new ArrayList<>();
        int deltaPp;
        int maxStar = Integer.MIN_VALUE;
        int minStar = Integer.MAX_VALUE;
        boolean tie = false;
        boolean winnerFound = false;

        //Apply building effects
        for (Player player : players){
            for (Building building : player.getBuildings()){
                building.getEffect().applyEffectEventShamanicRitual(player, building);
            }
        }

        //Look for max & min stars
        for (Player player : players) {
            int stars = player.getNumStars();

            if (stars > maxStar) {
                maxStar = stars;
            }
            if (stars < minStar) {
                minStar = stars;
            }
        }

        //Look for winners and losers, checking for possible ties
        for (Player player : players){
            if(player.getNumStars() == maxStar){
                if (winnerFound) {
                    tie = true;
                }
                winnerFound = true;
            }


        }

        //Award and deduct pps, checking for possible building effects
        for(Player player : players) {
            int stars = player.getNumStars();
            deltaPp = 0;

            if (stars == maxStar && player.getDoublePp() && !tie) {
                deltaPp = winnerPp * 2;
            } else if (stars == maxStar) {
                deltaPp = winnerPp;
            }

            if (stars == minStar && !player.getDontLosePp()){
                player.addPp(loserPp);
            }

            player.addPp(deltaPp);
            results.add(new EventResult("ShamanicRitual", this.id, player.toDTO(), deltaPp, 0));
        }

        return results;
    }

    @Override
    public String getName(){
        return name;
    }

    public ShamanicRitualDTO toDTO() {
        return new ShamanicRitualDTO(getWinnerPp(), getLoserPp(), getEra().name(), getId());
    }
}
