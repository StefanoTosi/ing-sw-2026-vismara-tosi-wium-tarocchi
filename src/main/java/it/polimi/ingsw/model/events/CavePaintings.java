package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Era;
import it.polimi.ingsw.model.Player;

/**
 * If you have less than x Artists you loose x pps,
 * otherwise if you have more or equal than x Artists you gain x pps
 */

public class CavePaintings extends Event {

    private int topPp;
    private int bottomPp;
    private int minArtist;

    public CavePaintings(int minArtist, Era era, int topPp, int bottomPp){
        super(era);
        this.minArtist = minArtist;
        this.topPp = topPp;
        this.bottomPp = bottomPp;
        this.name = "CavePaintings";
    }

    public void applyeffect(Player player){
        int numArtist = player.getNumArtists();
            if(numArtist < minArtist){
                player.addPp(-topPp);
            }else if(numArtist >= minArtist) {
                player.addPp(bottomPp);
            }
    }

    @Override
    public String getName(){return name;}

}
