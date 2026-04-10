package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess; // Your in-memory DAO implementation
import model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class JUNITTests {

    private GameService gameService;
    private DataAccess data;
    private String authToken;

    @BeforeEach
    void setup() throws Exception {
        data = new MemoryDataAccess();
        gameService = new GameService(data);
        UserData user = new UserData("joe", "123", "joe@joe.com");
        data.createUser(user);
        authToken = "test-token";
        data.createAuth(new AuthData(authToken, "joe"));
    }

    @Test
    void createGameSuccess() throws Exception {
        CreateGameRequest request = new CreateGameRequest("Test Game");
        CreateGameResponse response = gameService.createGame(authToken, request);
        assertNotNull(response);
        assertTrue(response.gameID() > 0);
        GameData game = data.getGame(response.gameID());
        assertEquals("Test Game", game.gameName());
        assertNull(game.whiteUsername());
        assertNull(game.blackUsername());
    }

    @Test
    void createGameUnauthorized() {
        CreateGameRequest request = new CreateGameRequest("Test Game");
        Exception exception = assertThrows(Exception.class, () -> gameService.createGame("bad-token", request));
        assertEquals("unauthorized", exception.getMessage());
    }

    @Test
    void listGamesSuccess() throws Exception {
        int id = data.createGame("Game 1");
        ListGamesResponse response = gameService.listGames(authToken);
        assertNotNull(response);
        List<GameData> games = response.games();
        assertEquals(1, games.size());
        assertEquals("Game 1", games.get(0).gameName());
    }

    @Test
    void listGamesUnauthorized() {
        Exception exception = assertThrows(Exception.class, () -> gameService.listGames("bad-token"));
        assertEquals("unauthorized", exception.getMessage());
    }

    @Test
    void joinGameSuccessWhite() throws Exception {
        int gameId = data.createGame("Join Test");
        JoinGameRequest request = new JoinGameRequest("WHITE", gameId);
        gameService.joinGame(authToken, request);
        GameData game = data.getGame(gameId);
        assertEquals("joe", game.whiteUsername());
        assertNull(game.blackUsername());
    }

    @Test
    void joinGameSuccessBlack() throws Exception {
        int gameId = data.createGame("Join Test 2");
        data.updateGame(new GameData(gameId, "bob", null, "Join Test 2", null));
        JoinGameRequest request = new JoinGameRequest("BLACK", gameId);
        gameService.joinGame(authToken, request);
        GameData game = data.getGame(gameId);
        assertEquals("joe", game.blackUsername());
        assertEquals("bob", game.whiteUsername());
    }

    @Test
    void joinGameAlreadyTaken() throws Exception {
        int gameId = data.createGame("Taken Test");
        data.updateGame(new GameData(gameId, "bob", null, "Taken Test", null));
        JoinGameRequest request = new JoinGameRequest("WHITE", gameId);
        Exception exception = assertThrows(Exception.class, () -> gameService.joinGame(authToken, request));
        assertEquals("already taken", exception.getMessage());
    }

    @Test
    void joinGameBadRequestInvalidColor() throws Exception {
        int gameId = data.createGame("Bad Color Test");
        JoinGameRequest request = new JoinGameRequest("GREEN", gameId);
        Exception exception = assertThrows(Exception.class, () -> gameService.joinGame(authToken, request));
        assertEquals("bad request", exception.getMessage());
    }

    @Test
    void joinGameUnauthorized() throws Exception {
        int gameId = data.createGame("Unauthorized Test");
        JoinGameRequest request = new JoinGameRequest("WHITE", gameId);
        Exception exception = assertThrows(Exception.class, () -> gameService.joinGame("bad-token", request));
        assertEquals("unauthorized", exception.getMessage());
    }
}
