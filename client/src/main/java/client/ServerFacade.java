package client;

import com.google.gson.Gson;
import model.*;
import java.net.URI;
import java.util.List;

import exception.ResponseException;

import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;


public class ServerFacade {
    private final String serverUrl;
    private final HttpClient client = HttpClient.newHttpClient();
    private String authToken;
    private List<GameData> gameList;
    private int port;
    private final Gson gson = new Gson();

    public ServerFacade(int port){
        this.port = port;
        serverUrl = "http://localhost:" + port;
    }

    public AuthData register(String username, String password, String email) throws ResponseException{
        var body = new RegisterRequest(username, password, email);
        var request = buildRequest("POST", "/user", body, null);
        var response = sendRequest(request);
        AuthData authData = handleResponse(response, AuthData.class);
        assert authData != null;
        this.authToken = authData.authToken();
        return authData;
    }

    public AuthData login(String username, String password) throws ResponseException{
        var body = new LoginReq(username, password);
        var request = buildRequest("POST", "/session", body, null);
        var response = sendRequest(request);
        AuthData authData = handleResponse(response, AuthData.class);
        assert authData != null;
        this.authToken = authData.authToken();
        return authData;
    }

    public void logout() throws ResponseException{
        assert authToken != null;
        var request = buildRequest("DELETE", "/session", null, authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
        this.authToken = null;
        this.gameList = null;
    }

    public int createGame(String gameName) throws ResponseException{
        var body = new CreateGameRequest(gameName);
        var request = buildRequest("POST", "/game", body, authToken);
        var response = sendRequest(request);
        GameData gameData = handleResponse(response, GameData.class);
        assert gameData != null;
        return gameData.gameID();
    }

    public List<GameData> listGames() throws ResponseException{
        assert authToken != null;
        var request = buildRequest("GET", "/game", null, authToken);
        var response = sendRequest(request);
        ListGamesResponse listResponse = handleResponse(response, ListGamesResponse.class);
        if(listResponse == null || listResponse.games() == null){
            throw new ResponseException(ResponseException.Code.ServerError, "Invalid response from server");
        }
        gameList = listResponse.games();
        return gameList;
    }

    public void joinGame(int gameNumber, String color) throws ResponseException{
        if(gameList == null || gameNumber < 1 || gameNumber > gameList.size()){
            throw new ResponseException(ResponseException.Code.ClientError, "invalid game number");
        }
        GameData game = gameList.get(gameNumber - 1);
        int gameID = game.gameID();
        var body = new JoinGameRequest(color, gameID);
        var request = buildRequest("PUT", "/game", body, authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public void observeGame(int gameNumber) throws ResponseException{
        if(gameList == null || gameNumber < 1 || gameNumber > gameList.size()){
            throw new ResponseException(ResponseException.Code.ClientError, "invalid game number");
        }
    }

    public int getPort() {
        return port;
    }

    public String getAuthToken(){
        return authToken;
    }

    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null){
            request.setHeader("Content-Type", "application/json");
        }
        if(authToken != null){
            request.setHeader("Authorization", authToken);
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request){
        if (request != null){
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else{
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException{
        try{
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex){
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException{
        var status = response.statusCode();
        if (!isSuccessful(status)){
            var body = response.body();
            if (body != null){
                try{
                    throw ResponseException.fromJson(body);
                }
                catch (Exception exception){
                    throw new ResponseException(ResponseException.Code.ServerError, "Server error:" + body);
                }
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        if (responseClass != null){
            try {
                return new Gson().fromJson(response.body(), responseClass);
            }
            catch (Exception exception) {
                throw new ResponseException(ResponseException.Code.ServerError, "Invalid JSON response" + response.body());
            }
        }

        return null;
    }

    private boolean isSuccessful(int status){
        return status / 100 == 2;
    }

    public Gson getGson(){
        return gson;
    }
}
