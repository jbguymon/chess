package client;

import chess.*;
import ui.ClientUI;

public class ClientMain {
    public static void main(String[] args) {
        var ui = new ClientUI("http://localhost:8080");
        ui.run();
    }
}
