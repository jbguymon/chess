package dataaccess;
import model.*;
import java.util.*;

public class MemoryDataAccess implements dataaccess {
    private final Map<String, UserData> users = new HashMap<>();
    private final Map<String, AuthData> authTokens = new HashMap<>();
    private final Map<Integer, GameData> games = new HashMap<>();
    private int nextGameId = 1;

    public void clear(){
        users.clear();
        authTokens.clear();
        games.clear();
        nextGameId = 1;
    }

    public void createUser(UserData user) throws DataAccessException{
        users.put(user.username(), user);
    }

    public UserData getUser(String username){
        return users.get(username);
    }

    public void createAuth(AuthData auth){
        authTokens.put(auth.authToken(), auth);
    }

    public AuthData getAuth(String authToken){
        return authTokens.get(authToken);
    }

    public void deleteAuth(String authToken){
        authTokens.remove(authToken);
    }

    public int createGame(String gameName){
        int gameID = nextGameId++;
        GameData game = new GameData(gameID, null, null, gameName, null);
        games.put(gameID, game);
        return gameID;
    }

    public GameData getGame(int gameID){
        return games.get(gameID);
    }

    public List<GameData> listGames(){
        return new ArrayList<>(games.values());
    }

    public void updateGame(GameData game){
        games.put(game.gameID(), game);
    }
}

