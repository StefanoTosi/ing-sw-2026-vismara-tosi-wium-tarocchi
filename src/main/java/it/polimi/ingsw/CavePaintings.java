package it.polimi.ingsw;

/**
 * If you have less then x Artists you loose x pps,
 * otherwise if you have more or equal than x Artists you gain x pps
 */

public class CavePaintings extends Event {

    private Era era;

    private int minArtist;

    public CavePaintings(int minArtist, Era era){
        this.minArtist = minArtist;
        this.era = era;
        this.name = "CavePaintings";
    }

    public void applyeffect(Player player){
        int numArtist = player.getNumArtists();
        if(numArtist > minArtist){
            if(numArtist < minArtist){
                player.addPp(-minArtist);
            }else if(numArtist >= minArtist){
                player.addPp(minArtist);
            }
        }
    }

    @Override
    public Era getEra(){
        return era;
    }

    @Override
    public String getName(){return name;}

}
