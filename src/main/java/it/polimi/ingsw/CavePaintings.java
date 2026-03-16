package it.polimi.ingsw;

public class CavePaintings extends Event {
    private final String NAME = "CavePaintings";
    // se si hanno un numero di carte artista pari a quelli segnati sopra si ottengono i punti sopra,
    // altimenti se si hanno quelli sotto si tolgono i punti sotto
    private int topNumArtist;
    private int topPp;
    private int bottomNumArtist;
    private int bottomPp;
    // per essere usata la carta CavePaintings il giocatore deve avere in mano minimo tot Artisti
    private int minArtist;

    public CavePaintings(int topNumArtist, int topPp, int bottomNumArtist, int bottomPp, int minArtist){

    }

    public void applyeffect(Player player){
        int numArtist = player.getNumArtists();
        if(numArtist > minArtist){
            if(numArtist == topNumArtist){
                player.addPp(-topPp);
            }else if(numArtist == bottomNumArtist){
                player.addPp(bottomPp);
            }
        }
    }

    public int getNumArtist(Player player) {
        return player.getNumArtists();
    }

    @Override
    public Era getEra(){return null;}

    @Override
    public String getName(){
        return NAME;
    }

}
