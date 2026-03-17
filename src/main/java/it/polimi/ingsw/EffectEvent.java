package it.polimi.ingsw;

import java.util.function.*;

public class EffectEvent extends Effect{

    private int additionalStars;
    private BiConsumer<Player, Event> effect;

    EffectEvent(int additionalStars, BiConsumer<Player, Event> effect) {
        this.additionalStars = additionalStars;
        this.effect = effect;
    }

    @Override
    public void applyEffectEvent(Player player, Event event){
        effect.accept(player, event);
    }

    // Ci sono casi in cui si contano le stelle senza quelle aggiuntive dei building?
    // Altrimenti questo metodo si può rimuovere
    @Override
    public int getAdditionalStarts() {
        return additionalStars;
    }
}

/*
 * Durante l’Evento Sostentamento, avete uno sconto di 1 Cibo sul totale che dovreste pagare, per ogni carta Personaggio del tipo indicato nella vostra tribù (Artisti/Inventori/Raccoglitori)
 * (p, e) -> {
 *      if (e instanceof  Sustenance) {
 *          p.addFood(p.getNumxxx()); // è possibile che si debba pagare 0 o mendo di questo?
 *      }
 *  }
 *
 * Durante l’Evento Rituale Sciamanico, non perdete Punti Prestigio se avete meno icone degli altri giocatori
 * (p, e) -> {
 *      if (e instanceof  ShamanicRitual) {
 *          ???
 *      }
 *  }
 *
 * Durante l’Evento Rituale Sciamanico, se avete più icone  di tutti gli altri giocatori, guadagnate il doppio dei PP indicati. Se guadagnate PP insieme ad altri giocatori (poiché avete lo stesso numero di icone ), non ne guadagnate il doppio
 * (p, e) -> {
 *      if (e instanceof  ShamanicRitual) {
 *          // Serve un riferimento a players!!!
 *      }
 *  }
 *
 * Durante l’Evento Pitture Rupestri, prendete 1 Cibo per ogni Artista nella vostra tribù
 * (p, e) -> {
 *      if (e instanceof  CavePaintings) {
 *          p.addFood(p.getNumArtists());
 *      }
 *  }
 *
 */