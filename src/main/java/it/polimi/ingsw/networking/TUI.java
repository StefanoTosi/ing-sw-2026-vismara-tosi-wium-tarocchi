package it.polimi.ingsw.networking;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.characters.Artist;
import it.polimi.ingsw.model.characters.Hunter;
import it.polimi.ingsw.model.characters.Inventor;
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

    @Override
    public void update() throws RemoteException, IllegalActionException {
        System.out.println("Ricevuto aggiornamento");
        //this.game = controller.getGame(client.getNickname());
    }
}

public void printBoard(Board board){
    printRowTribe(board.getTopRowTribe());
    printRowTribe(board.getBottomRowTribe());
    printRowBuilding(board.getTopRowBuilding());
    printRowBuilding(board.getBottomRowBuilding());
}

public void printRowTribe(List<Card> cards){

    StringBuilder line1 = new StringBuilder();
    StringBuilder line2 = new StringBuilder();
    StringBuilder line3 = new StringBuilder();
    StringBuilder line4 = new StringBuilder();
    StringBuilder line5 = new StringBuilder();

    for(Card card : cards) {
        line1.append("+-------+");
        line2.append("|" + card.getName() + "|");
        line3.append("|" + card.getEra() + "|");
        line4.append("| idk   |");
        line5.append("+-------+");

        System.out.println(line1);
        System.out.println(line2);
        System.out.println(line3);
        System.out.println(line4);
        System.out.println(line5);
    }
}

public void printRowBuilding(List<Building> buildings){
    StringBuilder line1 = new StringBuilder();
    StringBuilder line2 = new StringBuilder();
    StringBuilder line3 = new StringBuilder();
    StringBuilder line4 = new StringBuilder();
    StringBuilder line5 = new StringBuilder();

    for(Building building : buildings) {
        line1.append("+-------+");
        line2.append("| Build |");
        line3.append("|" + building.getCost() + "|");
        line4.append("| " + building.getEffect() + " |");
        line5.append("+-------+");
    }
}

public void printPlayerCards(List<Player> players){
    for(Player player : players){
        System.out.println("\n=== " + player.getName() + " ===");
        printRowBuilding(player.getBuildings());
        printArtist(player.getArtists());
        //printGatherer(player.getGatherers());
        printHunter(player.getHunters());
        printInventor(player.getInventors());
    }
}

public void printArtist(List<Artist> artists) {
    StringBuilder line1 = new StringBuilder();
    StringBuilder line2 = new StringBuilder();
    StringBuilder line3 = new StringBuilder();
    StringBuilder line4 = new StringBuilder();
    StringBuilder line5 = new StringBuilder();

    for (Artist artist : artists) {
        line1.append("+-------+");
        line2.append("|Artist |");
        line3.append("|" + artist.getEra() + "|");
        line4.append("|       |");
        line5.append("+-------+");
    }
}

public void printGatherer(List<Gatherer> gatherers) {
    StringBuilder line1 = new StringBuilder();
    StringBuilder line2 = new StringBuilder();
    StringBuilder line3 = new StringBuilder();
    StringBuilder line4 = new StringBuilder();
    StringBuilder line5 = new StringBuilder();

    for (Gatherer gatherer : gatherers) {
        line1.append("+-------+");
        line2.append("|Gather |");
        line3.append("|       |");
        line4.append("|       |");
        line5.append("+-------+");
    }
}

public void printHunter(List<Hunter> hunters) {
    StringBuilder line1 = new StringBuilder();
    StringBuilder line2 = new StringBuilder();
    StringBuilder line3 = new StringBuilder();
    StringBuilder line4 = new StringBuilder();
    StringBuilder line5 = new StringBuilder();

    for (Hunter hunter : hunters) {
        line1.append("+-------+");
        line2.append("|Hunter |");
        line3.append("| " + hunter.getEra() + " |");
        line4.append("| " + hunter.getIcon() + " |");
        line5.append("+-------+");
    }
}

public void printInventor(List<Inventor> inventors) {
    StringBuilder line1 = new StringBuilder();
    StringBuilder line2 = new StringBuilder();
    StringBuilder line3 = new StringBuilder();
    StringBuilder line4 = new StringBuilder();
    StringBuilder line5 = new StringBuilder();

    for (Inventor inventor : inventors) {
        line1.append("+-------+");
        line2.append("|Invent |");
        line3.append("|" + inventor.getEra() + "|");
        line4.append("|" + inventor.getInventionIcon() + "|");
        line5.append("+-------+");
    }
}

