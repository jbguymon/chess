package client;

import ui.ClientUI;

public class ClientMain {
    public static void main(String[] args) {
        int port = 8080;
        var ui = new ClientUI(port);
        ui.run();
    }
}
