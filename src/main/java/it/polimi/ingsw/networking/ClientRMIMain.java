package it.polimi.ingsw.networking;

import javax.naming.NamingException;
import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.io.File;
import java.util.UUID;


public class ClientRMIMain extends UnicastRemoteObject implements ClientCallBack{
    private static final int PORT = 1099;
    private static final String ID_FILE = "clientID";
    private final String uuid;
    private String nickname;

    protected ClientRMIMain(String nickname) throws RemoteException {
        this.nickname = nickname;
        this.uuid = getOrCreateID();
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUUID() {
        return uuid;
    }

    private String getOrCreateID() throws RemoteException {
        File file = new File(ID_FILE);
        try{
            if(file.exists()){
                Scanner sc = new Scanner(file);
                String id = sc.nextLine();
                sc.close();
                return id;
            } else {
                String id = UUID.randomUUID().toString();
                PrintWriter pw = new PrintWriter(file);
                pw.println(id);
                pw.close();
                return id;
            }
        } catch (Exception e){
            throw new RemoteException(e.getMessage());
        }
    }

    public void startGame(Controller controller) throws RemoteException {
        Scanner sc = new Scanner(System.in);
        while(!sc.equals("quit")){
            System.out.println("Welcome to ServerRMI, write something");
            String testo = sc.nextLine();

            controller.test(testo);
        }
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        System.out.println("Received message: " + message + "\n");
    }

    public static void main(String[] args) throws NamingException, RemoteException, NotBoundException {
        // Getting the registry
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", PORT);
        String remoteObjectName = "ServerRMI";

        Controller controller = (Controller) registry.lookup(remoteObjectName);

        Scanner sc = new Scanner(System.in);
        Boolean flag = true;

        ClientRMIMain client = new ClientRMIMain("");

        //Until a valid Nickname
        do {
            String nickname = sc.nextLine();
            int result = controller.addUser(client.getUUID(), nickname, client);

            if(result == 0){ 
                client.setNickname(nickname);
                flag = false;
            }
        } while (flag);

        //join or create a game
        boolean tmp = controller.joinGame();
        if(!tmp){
            int numPlayers = sc.nextInt();
            controller.createGame(numPlayers);
        }

        client.startGame(controller);
    }
}
