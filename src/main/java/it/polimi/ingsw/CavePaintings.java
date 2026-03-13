package it.polimi.ingsw;

public class CavePaintings extends Event {
    private String NAME = "CavePaintings";
    // se si hanno un numero di carte artista pari a quelli segnati sopra si ottengono i punti sopra,
    // altimenti se si hanno quelli sotto si tolgono i punti sotto
    private int topNumArtist;
    private int topPp;
    private int bottomNumArtist;
    private int bottomPp;
    // per essere usata la carta CavePaintings il giocatore deve avere in mano minimo tot Artisti
    private int minArtist;

    @Override
    public Era getEra(){return null;}

    @Override
    public String getName(){
        return NAME;
    }

}
