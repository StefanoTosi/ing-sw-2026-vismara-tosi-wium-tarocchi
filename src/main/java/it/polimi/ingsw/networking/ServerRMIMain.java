package it.polimi.ingsw.networking;
import java.rmi.*;
import java.rmi.registry.*;

public class ServerRMIMain {
    //default number of RMI Registry port
    private static final int PORT = 1099;

    public static void main(String[] args) throws RemoteException, AlreadyBoundException{
        ServerRMI server = new ServerRMI();

        //Bind remote object's stub in the registry
        Registry registry = LocateRegistry.createRegistry(PORT);
        registry.bind("ServerRMI", server);
        System.out.println("ServerRMI ready");
    }
}
