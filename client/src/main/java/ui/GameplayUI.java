package ui;

import client.NotificationHandler;
import client.WebSocketClient;
import java.util.Scanner;
import java.util.Objects;
import websocket.*;
import chess.ChessGame;
import client.ServerFacade;


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

    }

    private void sendConnectCommand(){

    }

    private void handleServerMessage(String json){

    }

    private void sendLeaveCommand(){

    }

    private void handleGameplayCommand(String input){

    }
}
