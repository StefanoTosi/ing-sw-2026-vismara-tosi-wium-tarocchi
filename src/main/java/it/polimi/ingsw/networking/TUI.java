package it.polimi.ingsw.networking;
import java.rmi.RemoteException;
import java.util.Scanner;


public class TUI {
    private Scanner in = new Scanner(System.in);
    private Controller controller;

    public TUI (Controller controller){
        this.controller = controller;
    }

    public static void main(String[] args) throws RemoteException {
        Controller controller1 = null;
        TUI tui = new TUI(controller1);
        tui.start();
    }

    public void start() throws RemoteException {
        while(true){
            showMenu();
            int input = Integer.parseInt(in.nextLine());
            handleInput(input);
        }
    }

    public void showMenu(){
        System.out.println("MENU:");
        System.out.println("1. Login");
        System.out.println("2. Play");
        System.out.println("3. Exit");
    }

    public void handleInput(int input) throws RemoteException {
        switch (input){
            case 1:
                System.out.println("Username:");
                String username = in.nextLine();

                break;
            case 2:
                System.out.println("Nickname:");
                String nickname = in.nextLine();
                ClientRMIMain client = new ClientRMIMain(nickname);
                String uuid =  client.getUUID();
                System.out.println("got uuid");
                controller.addUser(uuid, nickname, client); // qui abbiamo null pointer exeption
                break;
            case 3:
                System.exit(0);
            default:
                System.out.println("Invalid answer");
        }
    }
}
