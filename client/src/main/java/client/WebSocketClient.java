package client;

import jakarta.websocket.*;
import java.net.URI;

@ClientEndpoint
public class WebSocketClient {
    private Session session;

    public WebSocketClient(String url){
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

    }

    public void sendMessage(String message){

    }
}
