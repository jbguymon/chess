package server.websocket;


import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.*;
import chess.ChessGame;
import model.GameData;
import server.Server;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/ws")
public class WebSocketHandler {
    private static final ConcurrentHashMap<Integer, ConcurrentHashMap<Session, String>> gameSessions = new ConcurrentHashMap<>();
}
