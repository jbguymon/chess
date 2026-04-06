package client;

import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import model.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;


public class ServerFacadeTests {

    private static Server server;
    private ServerFacade facade;
    private static int port;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    private void clearData(){
        try{
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:" + port + "/db"))
                    .DELETE()
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception ignored){
        }
    }

    //register tests
    @Test
    void registerPositive() throws ResponseException {
        AuthData auth = facade.register("joe", "password123", "joe@joe.com");
        assertNotNull(auth);
        assertNotNull(auth.authToken());
        assertEquals("joe", auth.username());
    }

    @Test
    void registerNegative() throws ResponseException{
        facade.register("dupe", "password", "dupe@dupe.com");
        assertThrows(ResponseException.class, () ->
                facade.register("dupe", "password", "dupe@dupe.com"));
    }

    //login tests
    @Test
    void loginPositive() throws ResponseException{
        facade.register("loginuser", "loginpass", "login@login.com");
        AuthData auth = facade.login("loginuser", "loginpass");
        assertNotNull(auth.authToken());
    }

    @Test
    void loginNegative() throws ResponseException{
        facade.register("loginuserbad", "loginbadpass", "login@bad.com");
        assertThrows(ResponseException.class, () ->
                facade.login("loginuserbad", "notbadloginpass"));
    }

    //logout tests
    @Test
    void logoutPositive() throws ResponseException{
        AuthData auth = facade.register("logoutuser", "logoutpass", "logout@pos.com");
        assertNotNull(auth.authToken());
        assertDoesNotThrow(() -> facade.logout(auth.authToken()));
    }

    @Test
    void logoutNegative(){
        assertThrows(ResponseException.class, () -> facade.logout("badtoken"));
    }

    //create game tests
    @Test
    void createGamePositive() throws ResponseException{
        facade.register("creategameuser", "creategamepass", "create@game.com");
        int gameID = facade.createGame("testgame");
        assertTrue(gameID > 0);
    }

    @Test
    void createGameNegative() throws ResponseException{
        assertThrows(ResponseException.class, () -> facade.createGame("noauthgame"));
    }

    //list game tests
    @Test
    void listGamesPositive() throws ResponseException{
        AuthData auth = facade.register("listgameuser", "listgamepass", "list@game.com");
        facade.createGame("game1");
        facade.createGame("game2");
        List<GameData> games = facade.listGames(auth.authToken());
        assertEquals(2, games.size());
    }

    @Test
    void listGamesNegative() throws ResponseException{
        assertThrows(ResponseException.class, () -> facade.listGames("badToken"));
    }

    //join game tests
    @Test
    void joinGamePositive() throws ResponseException{
        AuthData auth = facade.register("joingameuser", "joingamepass", "join@game.com");
        facade.createGame("game1");
        facade.listGames(auth.authToken());
        assertDoesNotThrow(() -> facade.joinGame(1, "WHITE"));
    }

    void joinGameNegative() throws ResponseException{
        AuthData auth = facade.register("joingameuserbad", "joingamepassbad", "join@game.com");
        facade.createGame("game1");
        facade.listGames(auth.authToken());
        assertThrows(ResponseException.class, () -> facade.joinGame(2, "WHITE"));
    }


}
