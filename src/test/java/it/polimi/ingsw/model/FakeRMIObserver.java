package it.polimi.ingsw.model;

import it.polimi.ingsw.networking.RMI.ClientCallBack;

import java.rmi.RemoteException;

public class FakeRMIObserver implements ClientCallBack {
    public int updateCalls = 0;
    public int closingCalls = 0;

    @Override
    public void receiveMessage(String message) throws RemoteException {

    }

    @Override
    public void setNickname(String nickname) throws RemoteException {

    }

    @Override
    public String getNickname() throws RemoteException {
        return "";
    }

    @Override
    public void update(GameDTO game) {
        updateCalls++;
    }

    @Override
    public void closingGame(GameDTO game) {
        closingCalls++;
    }
}
