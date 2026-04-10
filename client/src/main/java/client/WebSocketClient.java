package client;

import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;

import java.net.URI;

@ClientEndpoint
public class WebSocketClient {
    private Session session;
    private final NotificationHandler handler;

    public WebSocketClient(String url, NotificationHandler handler){
        this.handler = handler;
        try{
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, URI.create(url));
        }
        catch(Exception exception){
            throw new RuntimeException("Failed to connect to server", exception);
        }
    }

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        System.out.println("Connected to server");
    }

    @OnMessage
    public void onMessage(String message){
        handler.notify(message);
    }

    public void sendMessage(String message){
        try{
            session.getBasicRemote().sendText(message);
        }
        catch(Exception exception){
            throw new RuntimeException(exception);
        }
    }

    public void close(){
        if(session != null && session.isOpen()){
            try{
                session.close();
            }
            catch(Exception exception){
            }
        }
    }
}
