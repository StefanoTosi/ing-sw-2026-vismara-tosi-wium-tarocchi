package it.polimi.ingsw.networking;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.effects.Building;
import it.polimi.ingsw.model.exceptions.IllegalActionException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class TUI implements UIObserver {
    private Scanner in = new Scanner(System.in);
    private Game game;
    private final ClientRMI client;
    private final Controller controller;

    public TUI () throws RemoteException, NotBoundException {
        this.client = new ClientRMI(this);
        this.controller = client.connectToServer();
        this.game = null;
    }

    public void start() throws RemoteException, IllegalActionException {
        showMenu();
        int input = Integer.parseInt(in.nextLine());
        handleInput(input);
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
                        //free the buffer
                        in.nextLine();
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

    public void main(String[] args) throws RemoteException, NotBoundException, IllegalActionException {
        TUI tui = new TUI();
        tui.start();
    }


    public void printBoard(Board board){
        printRowTribe(board.getTopRowTribe());
        printRowTribe(board.getBottomRowTribe());
        printRowBuilding(board.getTopRowBuilding());
        printRowBuilding(board.getBottomRowBuilding());
    }
    public String[] type = {"Event", "Chara", "Build"};
    public void printRowTribe(List<Card> cards){
        for(Card card : cards) {
            System.out.println("+-------+" +
                    "\n|" + Character.getName() + "|" +
                    "\n|  " + card.getEra() + "  |" +
                    "\n| idk   |" +
                    "\n+-------+");
        }
    }

    public void printRowBuilding(List<Building> buildings){
        for(Building building : buildings) {
            System.out.println("+-------+" +
                    "\n| Build |" +
                    "\n|  " + card.getEra() + "  |" +
                    "\n|effetto|" +
                    "\n+-------+");
        }
    }

}
public void printEvent(){
    System.out.println("________" +
            "\n| type   |" +
            "\n| pp     |" +
            "\n| idk    |" +
            "\n|________|");
}

    @Override
    public void update() throws RemoteException, IllegalActionException {
        System.out.println("Ricevuto aggiornamento");
        //this.game = controller.getGame(client.getNickname());
    }
}
