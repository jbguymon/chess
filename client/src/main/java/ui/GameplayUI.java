package ui;

import client.NotificationHandler;
import client.WebSocketClient;
import java.util.Scanner;
import java.util.Objects;

import com.google.gson.Gson;
import websocket.*;
import chess.ChessGame;
import client.ServerFacade;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;


public class GameplayUI {
    private final String authToken;
    private final int gameID;
    private final String playerColor;
    private final ServerFacade facade;
    private WebSocketClient webSocketClient;
    private ChessGame currentGame;

    public GameplayUI(String authToken, int gameID, String playerColor, ServerFacade facade){
        this.authToken = authToken;
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.facade = facade;
    }

    public void run(){
        NotificationHandler handler = this::handleServerMessage;
        String wsUrl = "ws://localhost:" + facade.getPort() + "/ws";
        this.webSocketClient = new WebSocketClient(wsUrl, handler);
        sendConnectCommand();
        runGamePlayLoop();
    }

    private void runGamePlayLoop() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type 'help' to see available commands.");
        while(true){
            System.out.print("game> ");
            String input = scanner.nextLine().trim();
            if(input.equalsIgnoreCase("leave")){
                sendLeaveCommand();
                webSocketClient.close();
                System.out.println("You left the game.");
                return;
            }
            handleGameplayCommand(input);
        }
    }

    private void sendConnectCommand(){
        try{
            Gson gson = new Gson();
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            String json = gson.toJson(command);
            webSocketClient.sendMessage(json);
        }
        catch (Exception exception){
            System.out.println("Failed to send CONNECT: " + exception.getMessage());
        }
    }

    private void handleServerMessage(String json){
        try{
            Gson gson = new Gson();
            ServerMessage message = gson.fromJson(json, ServerMessage.class);
            if(message == null){
                return;
            }
            switch (message.getServerMessageType()){
                case LOAD_GAME:
                    LoadGameMessage loadMsg = LoadGameMessage message;
                    this.currentGame = loadMsg.getGame();
                    boolean isWhite = Objects.equals(playerColor, "WHITE") || playerColor == null;
                    ChessBoardUI.displayBoard(currentGame.getBoard(), isWhite);
                    break;

                case NOTIFICATION:
                    NotificationMessage notif = NotificationMessage message;
                    System.out.println(notif.getMessage());
                    break;

                case ERROR:
                    ErrorMessage err = ErrorMessage message;
                    System.out.println("Error: " + err.getErrorMessage());
                    break;

                default:
                    System.out.println("Unknown message received.");
            }
        }
        catch(Exception exception){
            System.out.println("Failed to parse message: " + json);
            exception.printStackTrace();
        }
    }

    private void sendLeaveCommand(){
        try{
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            Gson gson = new Gson();
            String json = gson.toJson(command);
            webSocketClient.sendMessage(json);
        }
        catch(Exception exception){
            System.out.println("Failed to leave game.");
        }
    }

    private void handleGameplayCommand(String input){
        if(input.isBlank()){
            return;
        }
        String[] tokens = input.trim().split("\\s+");
        String command = tokens[0].toLowerCase();
        try{
            switch (command){
                case "help":
                    printGameplayHelp();
                    break;

                case "redraw":
                    redrawBoard();
                    break;

                case "move":
                    System.out.println("not handled yet.");
                    break;
                case "highlight":
                    System.out.println("not handled yet");
                    break;
                case "resign":
                    handleResign();
                    break;
                default:
                    System.out.println("Unknown Command.");
                    printGameplayHelp();
            }
        }
        catch(Exception exception){
            System.out.println("Error: " + exception.getMessage());
        }
    }

    private void printGameplayHelp(){
        System.out.println("""
                Gameplay Commands:
                move <start> <end>
                highlight <square>
                redraw
                resign
                leave
                help
                """);
    }

    private void redrawBoard(){
        if(currentGame == null){
            System.out.println("No board to display currently.");
            return;
        }
        boolean isWhite = Objects.equals(playerColor, "WHITE") || playerColor == null;
        ChessBoardUI.displayBoard(currentGame.getBoard(), isWhite);
    }

    private void handleResign(){
        System.out.print("Do you actually want to resign? (y/n): ");
        Scanner scanner = new Scanner(System.in);
        if(scanner.nextLine().trim().equalsIgnoreCase("y")){
            sendResignCommand();
        }
        else{
            System.out.println("Not resigning.");
        }
    }

    private void sendResignCommand(){
        try{
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            Gson gson = new Gson();
            String json = gson.toJson(command);
            webSocketClient.sendMessage(json);
        }
        catch (Exception exception){
            System.out.println("Failed to resign.");
        }
    }
}
