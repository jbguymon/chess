package ui;

import client.ServerFacade;
import model.*;

import java.util.Arrays;
import java.util.Scanner;
import java.util.List;

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
        switch(command){
            case "help":
                printHelpPreLogin();
                break;
            case "register":
                if(tokens.length != 4){
                    System.out.println("Incorrect usage. Correct usage: register <username> <password> <email>");
                    return;
                }
                facade.register(tokens[1], tokens[2], tokens[3]);
                System.out.println("Registered and automatically logged in.");
                isLoggedIn = true;
                break;
            case "login":
                if(tokens.length != 3){
                    System.out.println("Incorrect usage. Correct usage: login <username> <password>");
                    return;
                }
                facade.login(tokens[1], tokens[2]);
                System.out.println("Successfully logged in.");
                isLoggedIn = true;
                break;
            case "quit":
                System.exit(0);
                break;
            default:
                System.out.println("Command Unknown.");
                printHelpPreLogin();
        }
    }

    private void postLogin(String command, String[] tokens) throws Exception{
        switch(command){
            case "help":
                printHelpPostLogin();
                break;
            case "logout":
                facade.logout();
                isLoggedIn = false;
                System.out.println("Logged out.");
                break;
            case "create":
                if(tokens.length >= 2 && tokens[1].equalsIgnoreCase("game")){
                    if(tokens.length < 3){
                        System.out.println("Incorrect Usage. Correct Usage: create game <game name>");
                        return;
                    }
                    String gameName = String.join(" ", Arrays.copyOfRange(tokens, 2, tokens.length));
                    facade.createGame(gameName);
                    System.out.println("Game Successfully Created");
                }
                else{
                    System.out.println("Unknown command. Did you mean 'create game'?");
                }
                break;
            case "list":
                if(tokens.length == 2 && tokens[1].equalsIgnoreCase("games")) {
                    List<GameData> games = facade.listGames();
                    printGames(games);
                }
                else{
                    System.out.println("Unknown Command. Did you mean list games?");
                }
                break;
            case "play":
                if(tokens.length == 4 && tokens[1].equalsIgnoreCase("game")){
                    int gameNum = Integer.parseInt(tokens[2]);
                    String color = tokens[3].toUpperCase();
                    facade.joinGame(gameNum, color);
                    System.out.println("Successfully join game.");
                }
                else{
                    System.out.println("Unknown command. Correct usage: play game <number> <WHITE/BLACK");
                }
                break;
            case "observe":
                if(tokens.length == 3 && tokens[1].equalsIgnoreCase("game")){
                    int num = Integer.parseInt(tokens[2]);
                    facade.observeGame(num);
                    System.out.println("Successfully observing game");
                }
                else{
                    System.out.println("Unknown command. Correct Usage: observe game <number>");
                }
                break;
            case "quit":
                System.exit(0);
                break;
            default:
                System.out.println("Unknown command.");
                printHelpPostLogin();
        }
    }

    private void printHelpPreLogin(){
        System.out.println("""
                        Commands:
                        register <username> <password> <email>
                        login <username> <password>
                        help
                        quit
                """);
    }
    private void printHelpPostLogin(){
        System.out.println("""
                Commands:
                create game <game name>
                list games
                play game <game number> <WHITE/BLACK>
                observe game <game number>
                logout
                help
                quit""");
    }
    private void printGames(List<GameData> games){
        for(int i = 0; i < games.size(); i++){
            GameData gameData = games.get(i);
            System.out.printf("%d. %s (White: %s, Black: %s)%n", i + 1, gameData.gameName(),
                    gameData.whiteUsername() == null ? "-" : gameData.whiteUsername(),
                    gameData.blackUsername() == null ? "-" : gameData.blackUsername());
        }
    }
}
