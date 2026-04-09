package it.polimi.ingsw.networking;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class TUI{
    private Scanner in = new Scanner(System.in);
    private final ClientRMI client;
    private final Controller controller;

    public TUI () throws RemoteException, NotBoundException {
        this.client = new ClientRMI();
        this.controller = client.connectToServer();
    }

    public void start() throws RemoteException, IllegalActionException {
        while(true){
            showMenu();
            int input = Integer.parseInt(in.nextLine());
            handleInput(input);
        }
    }

    public void showMenu(){
        System.out.println("MENU:");
        System.out.println("1. Login");
        System.out.println("2. Exit");
    }

    public void handleInput(int input) throws RemoteException, IllegalActionException {
        switch (input){
            case 1:
                boolean flag = true;
                while(flag){
                    System.out.println("Username:");
                    String username = in.nextLine();
                    System.out.println("Password:");
                    String password = in.nextLine();
                    if(controller.addUser(password, username, client) == 0) flag = false;
                }
                if(!controller.joinGame(client.getNickname())){
                    int num;
                    System.out.println("No game found, let's create a new one!\n");
                    do {
                        System.out.println("How many players do you want?");
                        System.out.println("\n2 to 5 players");
                        num = in.nextInt();
                    }while(num < 2 || num > 5);
                    controller.createGame(client.getNickname(), num);
                }
                break;
            case 2:
                System.exit(0);
            default:
                System.out.println("Invalid input");
        }
    }

    public static void main(String[] args) throws RemoteException, NotBoundException, IllegalActionException {
        TUI tui = new TUI();
        tui.start();
    }
}
