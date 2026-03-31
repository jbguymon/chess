package server;

import chess.*;
import dataaccess.DatabaseInitializer;
import dataaccess.DatabaseManager;

public class ServerMain {
    public static void main(String[] args) {
        try{
            DatabaseInitializer.initialize();
        } catch (Exception exception){
            System.err.println(("Failed to make database: " + exception.getMessage()));
            System.exit(1);
        }
        Server server = new Server();
        server.run(8080);

        System.out.println("♕ 240 Chess Server");
    }
}
