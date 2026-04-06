package ui;

import client.ServerFacade;

import java.util.Scanner;

public class ClientUI {
    private final ServerFacade facade;
    private boolean isLoggedIn = false;

    public ClientUI(String serverURL){
        this.facade = new ServerFacade(serverURL);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("> ");
            String input = scanner.nextLine();
            try {
                handleCommand(input);
            } catch (Exception exception) {
                System.out.println("Error: " + exception.getMessage());
            }
        }
    }

    private void handleCommand(String input) throws Exception{
        String[] tokens = input.trim().split("\\s+");
        String command = tokens[0].toLowerCase();
        if(!isLoggedIn){
            preLogin(command, tokens);
        }
        else{
            postLogin(command, tokens);
        }
    }

    private void preLogin(String command, String[] tokens) throws Exception{

    }

    private void postLogin(String command, String[] tokens) throws Exception{

    }
}
