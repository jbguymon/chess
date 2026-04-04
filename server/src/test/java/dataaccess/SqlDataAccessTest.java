package dataaccess;
import model.*;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SqlDataAccessTest {
    private MySqlDataAccess data;
    @BeforeEach
    void setUp() throws DataAccessException {
        DatabaseInitializer.initialize();
        data = new MySqlDataAccess();
        data.clear();
    }

    //user tests

    @Test
    void createUserPositive() throws DataAccessException {
        UserData user = new UserData("joe", "password", "joe@joe.com");
        data.createUser(user);
        UserData result = data.getUser("joe");
        assertNotNull(result);
        assertEquals("joe", result.username());
        assertNotEquals("password", result.password());
    }

    @Test
    void createUserNegative_duplicate() throws DataAccessException {
        UserData user = new UserData("joe", "password", "joe@joe.com");
        data.createUser(user);
        assertThrows(DataAccessException.class, () -> data.createUser(user));
    }

    @Test
    void getUserPositive() throws DataAccessException {
        data.createUser(new UserData("amy", "pass", "a@joe.com"));
        UserData user = data.getUser("amy");
        assertNotNull(user);
        assertEquals("amy", user.username());
    }

    @Test
    void getUserNegative_notFound() throws DataAccessException {
        UserData user = data.getUser("random");
        assertNull(user);
    }

    //auth tests

    @Test
    void createAuthPositive() throws DataAccessException {
        data.createUser(new UserData("joe", "pass", "joe@joe.com"));
        AuthData auth = new AuthData("token", "joe");
        data.createAuth(auth);
        AuthData result = data.getAuth("token");
        assertNotNull(result);
        assertEquals("joe", result.username());
    }

    @Test
    void createAuthNegative_invalidUser() {
        AuthData auth = new AuthData("badToken", "random");
        assertThrows(DataAccessException.class, () -> data.createAuth(auth));
    }

    @Test
    void getAuthPositive() throws DataAccessException {
        data.createUser(new UserData("joe", "pass", "joe@joe.com"));
        data.createAuth(new AuthData("token", "joe"));
        AuthData auth = data.getAuth("token");
        assertNotNull(auth);
    }

    @Test
    void getAuthNegative_notFound() throws DataAccessException {
        assertNull(data.getAuth("fake"));
    }

    @Test
    void deleteAuthPositive() throws DataAccessException {
        data.createUser(new UserData("joe", "pass", "joe@joe.com"));
        data.createAuth(new AuthData("token", "joe"));
        data.deleteAuth("token");
        assertNull(data.getAuth("token"));
    }

    @Test
    void deleteAuthNegative_noError() {
        assertDoesNotThrow(() -> data.deleteAuth("fake"));
    }

    //game tests

    @Test
    void createGamePositive() throws DataAccessException {
        int id = data.createGame("game1");
        GameData game = data.getGame(id);
        assertNotNull(game);
        assertEquals("game1", game.gameName());
    }

    @Test
    void createGameNegative_nullName() {
        assertThrows(DataAccessException.class, () -> data.createGame(null));
    }

    @Test
    void getGamePositive() throws DataAccessException {
        int id = data.createGame("game1");
        GameData game = data.getGame(id);
        assertNotNull(game);
    }

    @Test
    void getGameNegative_notFound() throws DataAccessException {
        assertNull(data.getGame(2));
    }

    @Test
    void listGamesPositive() throws DataAccessException {
        data.createGame("game1");
        data.createGame("game2");
        List<GameData> games = data.listGames();
        assertEquals(2, games.size());
    }

    @Test
    void listGamesNegative_empty() throws DataAccessException {
        List<GameData> games = data.listGames();
        assertTrue(games.isEmpty());
    }

    @Test
    void updateGamePositive() throws DataAccessException {
        int id = data.createGame("game1");
        GameData game = data.getGame(id);
        GameData updated = new GameData(
                id,
                "white",
                null,
                "game1",
                new ChessGame());
        data.updateGame(updated);
        GameData result = data.getGame(id);
        assertEquals("white", result.whiteUsername());
    }

    @Test
    void updateGameNegative_invalidID() {
        GameData fake = new GameData(999, "w", "b", "fake", new ChessGame());
        assertDoesNotThrow(() -> data.updateGame(fake));
    }

    //test clear

    @Test
    void clearPositive() throws DataAccessException {
        data.createGame("game1");
        data.createUser(new UserData("joe", "pass", "joe@joe.com"));
        data.clear();
        assertTrue(data.listGames().isEmpty());
        assertNull(data.getUser("joe"));
    }
}
