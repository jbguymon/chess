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


}
