package dataaccess;
import java.sql.*;
import java.util.*;

import chess.ChessGame;
import model.*;
import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;

public class MySqlDataAccess implements DataAccess {
    private final Gson gson = new Gson();

    //User functions

    public void createUser(UserData user) throws DataAccessException {
        String sql = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
            var state = conn.prepareStatement(sql)){
            String hash = BCrypt.hashpw(user.password(), BCrypt.gensalt());
            state.setString(1, user.username());
            state.setString(2, hash);
            state.setString(3, user.email());
            state.executeUpdate();
        }
        catch (Exception exception){
            throw new DataAccessException("Error creating user", exception);
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        String sql = "SELECT username, password, email FROM user WHERE username=?";
        try(var conn = DatabaseManager.getConnection();
        var state = conn.prepareStatement(sql)) {
            state.setString(1, username);
            var resultSet = state.executeQuery();
            if(resultSet.next()){
                return new UserData(
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("email"));
            }
            return null;
        }
        catch (Exception exception){
            throw new DataAccessException("Error getting user", exception);
        }
    }

    //Auth token functions

    public void createAuth(AuthData auth) throws DataAccessException {
        String sql = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        try(var conn = DatabaseManager.getConnection();
        var state = conn.prepareStatement(sql)) {
            state.setString(1, auth.authToken());
            state.setString(2, auth.username());
            state.executeUpdate();
        }
        catch (Exception exception){
            throw new DataAccessException("Error creating auth", exception);
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        String sql = "SELECT authToken, username FROM auth WHERE authToken=?";
        try(var conn = DatabaseManager.getConnection();
        var state = conn.prepareStatement(sql)){
            state.setString(1, authToken);
            var resultSet = state.executeQuery();
            if(resultSet.next()){
                return new AuthData(resultSet.getString("authToken"),
                        resultSet.getString("username"));
            }
            return null;
        }
        catch (Exception exception){
            throw new DataAccessException("Error getting auth", exception);
        }
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        String sql = "DELETE FROM auth WHERE authToken=?";
        try(var conn = DatabaseManager.getConnection();
        var state = conn.prepareStatement(sql)){
            state.setString(1, authToken);
            state.executeUpdate();
        }
        catch (Exception exception){
            throw new DataAccessException("Error deleting auth", exception);
        }
    }

    //Game functions

    public int createGame(String gameName) throws DataAccessException {
        String sql = "INSERT INTO game (gameName, gameState) VALUES (?, ?)";
        String gameStateJson = gson.toJson(new ChessGame());
        try(var conn = DatabaseManager.getConnection();
        var state = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            state.setString(1, gameName);
            state.setString(2, gameStateJson);
            state.executeUpdate();
            var resultSet = state.getGeneratedKeys();
            if(resultSet.next()){
                return resultSet.getInt(1);
            }
            throw new DataAccessException("Failed to get game ID", null);
        }
        catch (Exception exception) {
            throw new DataAccessException("Error creating game", exception);
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        String sql = "SELECT * FROM game WHERE gameID=?";
        try(var conn = DatabaseManager.getConnection();
        var state = conn.prepareStatement(sql)) {
            state.setInt(1, gameID);
            var resultSet = state.executeQuery();
            if(resultSet.next()) {
                return new GameData(
                    resultSet.getInt("gameID"),
                    resultSet.getString("whiteUsername"),
                    resultSet.getString("blackUsername"),
                    resultSet.getString("gameName"),
                    gson.fromJson(resultSet.getString("gameState"), ChessGame.class)
                );
            }
            return null;
        }
        catch (Exception exception) {
            throw new DataAccessException("Error getting game", exception);
        }
    }

    public List<GameData> listGames() throws DataAccessException {
        String sql = "SELECT * FROM game";
        List<GameData> gameList = new ArrayList<>();
        try(var conn = DatabaseManager.getConnection();
            var state = conn.prepareStatement(sql)) {
            var resultSet = state.executeQuery();
            while(resultSet.next()){
                gameList.add(new GameData(
                   resultSet.getInt("gameID"),
                   resultSet.getString("whiteUsername"),
                   resultSet.getString("blackUsername"),
                   resultSet.getString("gameName"),
                   gson.fromJson(resultSet.getString("gameState"), ChessGame.class)
                ));
            }
            return gameList;
        }
        catch (Exception exception){
            throw new DataAccessException("Error listing games", exception);
        }
    }

    public void updateGame(GameData game) throws DataAccessException {
        String sql = "UPDATE game SET whiteUsername=?, blackUsername=?, gameState=? WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection();
             var state = conn.prepareStatement(sql)) {
            state.setString(1, game.whiteUsername());
            state.setString(2, game.blackUsername());
            state.setString(3, gson.toJson(game.game()));
            state.setInt(4, game.gameID());
            state.executeUpdate();
        }
        catch (Exception exception){
            throw new DataAccessException("Error updating game", exception);
        }
    }

    public void clear() throws DataAccessException{
        try (var conn = DatabaseManager.getConnection();
             var state = conn.createStatement()) {
            state.executeUpdate("DELETE FROM auth");
            state.executeUpdate("DELETE FROM game");
            state.executeUpdate("DELETE FROM user");
        }
        catch (Exception exception){
            throw new DataAccessException("Error clearing database", exception);
        }
    }
}
