package service;
import chess.ChessGame;
import dataaccess.DataAccess;
import model.*;
import java.util.List;

public class GameService {
    private final DataAccess data;

    public GameService(DataAccess data){
        this.data = data;
    }

    public CreateGameResponse createGame(String authToken, CreateGameRequest request) throws Exception {
        if(data.getAuth(authToken) == null || authToken == null){
            throw new Exception("unauthorized");
        }
        if(request.gameName() == null){
            throw new Exception("bad request");
        }
        int gameID = data.createGame(request.gameName());
        return new CreateGameResponse(gameID);
    }

    public ListGamesResponse listGames(String authToken) throws Exception {
        if(data.getAuth(authToken) == null || authToken == null){
            throw new Exception("unauthorized");
        }
        List<GameData> gameList = data.listGames();
        return new ListGamesResponse(gameList);
    }

    public void joinGame(String authToken, JoinGameRequest request) throws Exception {
        if(data.getAuth(authToken) == null){
            throw new Exception("unauthorized");
        }
        GameData game = data.getGame(request.gameID());
        if(game == null){
            throw new Exception("bad request");
        }
        String username = data.getAuth(authToken).username();
        if(request.playerColor() == null){
            throw new Exception("bad request");
        }
        if(request.playerColor().equals("WHITE")){
            if(game.whiteUsername() != null){
                throw new Exception("already taken");
            }
            game = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
        }
        else if(request.playerColor().equals("BLACK")){
            if(game.blackUsername() != null){
                throw new Exception("already taken");
            }
            game = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
        }
        else{
            throw new Exception("bad request");
        }
        data.updateGame(game);
    }
}
