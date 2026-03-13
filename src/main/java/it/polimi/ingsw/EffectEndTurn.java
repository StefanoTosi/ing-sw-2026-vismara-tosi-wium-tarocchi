package it.polimi.ingsw;

public class EffectEndTurn extends Effect{
    @Override
    public boolean checkEndGame() {
        return false;
    }

    @Override
    public boolean checkEndRound(){
        return false;
    }

}
