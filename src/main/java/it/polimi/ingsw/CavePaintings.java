package it.polimi.ingsw;

public class CavePaintings extends Event {
    public String NAME = "CavePaintings";
    // se si hanno un numero di carte artista pari a quelli segnati sopra si ottengono i punti sopra,
    // altimenti se si hanno quelli sotto si tolgono i punti sotto
    public int topNumArtist;
    public int topPp;
    public int bottomNumArtist;
    public int bottomPp;
    // per essere usata la carta CavePaintings il giocatore deve avere in mano minimo tot Artisti
    public int minArtist;

    @Override
    public Era getEra(){return null;}

    @Override
    public String getName(){
        return NAME;
    }

    @Override
    public void applyEffect(Player p){

    }
}
