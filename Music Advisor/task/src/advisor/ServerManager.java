package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.ServerSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ServerManager {
    boolean siteHit = false;
    private static volatile boolean shouldRun = true;
    HttpServer server;
    HttpClient client;
    String code = "";
    String alternateURL;
    String clientId = "xxxxxxxxxxxxxx";
    String clientSecret = "xxxxxxxxxxxxxxx";
    String accessToken = "";
    String apiServerPath;

    //ServerSocket socket = new ServerSocket(8080);
    ServerManager(String alternateURL, String apiServerPath) throws IOException {

        //server = HttpServer.create(new InetSocketAddress(0), 0);
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        client = HttpClient.newBuilder().build();
        this.alternateURL = alternateURL;
        this.apiServerPath = apiServerPath;
    }
    public void runServer(){
        server.createContext("/",
                new HttpHandler() {


                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        String pageResponse = "";
                        if(exchange.getRequestURI().getQuery() == null){
                            pageResponse = "Authorization code not found. Try again.";
                            exchange.sendResponseHeaders(400, pageResponse.length());
                            exchange.getResponseBody().write(pageResponse.getBytes());
                            exchange.getResponseBody().close();
                        }else {
                            String query = exchange.getRequestURI().getQuery();
                            Map<String, String> queryParams = parseQuery(query);

                            if (queryParams.get("code") != null) {
                                code = queryParams.get("code");
                                pageResponse = "Got the code. Return back to your program.";
                                shouldRun = false;
                            } else {
                                pageResponse = "Authorization code not found. Try again.";

                            }
                        }

                        //String query = exchange.getRequestURI().getQuery();
                        exchange.sendResponseHeaders(200, pageResponse.length());
                        exchange.getResponseBody().write(pageResponse.getBytes());
                        exchange.getResponseBody().close();

                        logSiteHit();


                    }
                }
        );
        server.start();

    }

    public void requestSpotifyCategories(){

    }
    public void requestSpotifyCode() throws InterruptedException, IOException{

        // Create the authorization header value
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        String authorizationHeader = "Basic " + encodedAuth;
        String formData = null;
        String redirectUri = "http://localhost:8080";
        try {
            formData = "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8) +
                    "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                    "&grant_type=" + URLEncoder.encode("authorization_code", StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String bodyCode = "code&" + code + "&redirect_uri=http://localhost:8080&grant_type=authorization_code" ;

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", authorizationHeader)
                .uri(URI.create(alternateURL +"/api/token"))
                //.uri(URI.create("https://accounts.spotify.com/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        storeAccessToken(response.body());

    }

    public void printCategories() throws InterruptedException, IOException{
        JsonObject categoryObject = callApi("categories");
        JsonArray itemsArray = categoryObject.getAsJsonObject("categories").getAsJsonArray("items");
        List<String> categories = new ArrayList<>();
        for(JsonElement itemElement : itemsArray) {
            JsonObject itemObject = itemElement.getAsJsonObject();
            String name = itemObject.get("name").getAsString();
            categories.add(name);
        }
        for(String category: categories){
            System.out.println(category);
        }

    }
    public List<String> getCategoriesList()throws InterruptedException, IOException{
        JsonObject categoryObject = callApi("categories");
        JsonArray itemsArray = categoryObject.getAsJsonObject("categories").getAsJsonArray("items");
        List<String> categories = new ArrayList<>();
        for(JsonElement itemElement : itemsArray) {
            JsonObject itemObject = itemElement.getAsJsonObject();
            String name = itemObject.get("name").getAsString();
            categories.add(name);
        }
        return categories;
    }
    public String getCategoryId(String name)throws InterruptedException, IOException{
        JsonObject categoryObject = callApi("categories");
        JsonArray itemsArray = categoryObject.getAsJsonObject("categories").getAsJsonArray("items");
        for(JsonElement itemElement : itemsArray) {
            JsonObject itemObject = itemElement.getAsJsonObject();
            if (name.equals(itemObject.get("name").getAsString())) {
                return itemObject.get("id").getAsString();
            }
        }
        return null;
    }

    public void printNew() throws InterruptedException, IOException{
        JsonObject newItemsObject = callApi("new-releases");
        JsonArray itemsArray = newItemsObject.getAsJsonObject("albums").getAsJsonArray("items");
        for(JsonElement itemElement: itemsArray){
            JsonObject itemObject = itemElement.getAsJsonObject();
            System.out.println(itemObject.get("name").getAsString());
            JsonArray artistArray = itemObject.getAsJsonArray("artists");
            List<String> artists = new ArrayList<>();
            for (JsonElement artistElement : artistArray){
                JsonObject artistObject = artistElement.getAsJsonObject();
                artists.add(artistObject.get("name").getAsString());
            }
            System.out.println(artists);
            System.out.println(itemObject.getAsJsonObject("external_urls").get("spotify").getAsString());
            System.out.println();
        }

    }

    public List<NewAlbumItem> getNewAlbumList() throws InterruptedException, IOException {
        List<NewAlbumItem> newAlbumItemList = new ArrayList<>();
        JsonObject newItemsObject = callApi("new-releases");
        JsonArray itemsArray = newItemsObject.getAsJsonObject("albums").getAsJsonArray("items");
        for(JsonElement itemElement: itemsArray){
            JsonObject itemObject = itemElement.getAsJsonObject();
            String name = itemObject.get("name").getAsString();
            JsonArray artistArray = itemObject.getAsJsonArray("artists");
            List<String> artists = new ArrayList<>();
            for (JsonElement artistElement : artistArray){
                JsonObject artistObject = artistElement.getAsJsonObject();
                artists.add(artistObject.get("name").getAsString());
            }

            String url = itemObject.getAsJsonObject("external_urls").get("spotify").getAsString();
            NewAlbumItem newAlbumItem = new NewAlbumItem(name, artists, url);
            newAlbumItemList.add(newAlbumItem);
        }
        return newAlbumItemList;
    }

    public void printFeatured()throws InterruptedException, IOException{
        JsonObject newItemsObject = callApi("featured-playlists");
        JsonArray itemsArray = newItemsObject.getAsJsonObject("playlists").getAsJsonArray("items");
        for(JsonElement itemElement: itemsArray){
            JsonObject itemObject = itemElement.getAsJsonObject();
            System.out.println(itemObject.get("name").getAsString());
            System.out.println(itemObject.getAsJsonObject("external_urls").get("spotify").getAsString());
            System.out.println();
        }

    }
    public List<PlaylistItem> getFeatured()throws InterruptedException, IOException {
        JsonObject newItemsObject = callApi("featured-playlists");
        JsonArray itemsArray = newItemsObject.getAsJsonObject("playlists").getAsJsonArray("items");
        List<PlaylistItem> playlistItemList = new ArrayList<>();
        for(JsonElement itemElement: itemsArray){
            JsonObject itemObject = itemElement.getAsJsonObject();
            String name = itemObject.get("name").getAsString();
            String url = itemObject.getAsJsonObject("external_urls").get("spotify").getAsString();
            playlistItemList.add(new PlaylistItem(name, url));
        }
        return playlistItemList;
    }

    public void printSelectedPlaylist(String playlistType)throws InterruptedException, IOException{
        String id = getCategoryId(playlistType);
        if (id == null){
            id = "nonexistentcategory";
        }
        JsonObject newItemsObject = callApi("categories/" + id + "/playlists");
        if (newItemsObject.getAsJsonObject("error")!= null){
            System.out.println(newItemsObject.getAsJsonObject());
            //System.out.println(newItemsObject.getAsJsonObject("message").getAsString());
            return;
        }
        if(newItemsObject.getAsJsonObject("playlists") == null){
            System.out.println("Unknown category name.");
            return;
        }
        JsonArray itemsArray = newItemsObject.getAsJsonObject("playlists").getAsJsonArray("items");
        for(JsonElement itemElement: itemsArray){
            JsonObject itemObject = itemElement.getAsJsonObject();
            System.out.println(itemObject.get("name").getAsString());
            System.out.println(itemObject.getAsJsonObject("external_urls").get("spotify").getAsString());
            System.out.println();
        }

    }

    public List<PlaylistItem> getSelectedPlaylist(String playlistType) throws InterruptedException, IOException {
        List<PlaylistItem> playlistItemList = new ArrayList<>();
        String id = getCategoryId(playlistType);
        if (id == null){
            id = "nonexistentcategory";
        }
        JsonObject newItemsObject = callApi("categories/" + id + "/playlists");
        if (newItemsObject.getAsJsonObject("error")!= null){
            System.out.println(newItemsObject.getAsJsonObject());
            //System.out.println(newItemsObject.getAsJsonObject("message").getAsString());
            return null;
        }
        if(newItemsObject.getAsJsonObject("playlists") == null){
            System.out.println("Unknown category name.");
            return null;
        }
        JsonArray itemsArray = newItemsObject.getAsJsonObject("playlists").getAsJsonArray("items");
        for(JsonElement itemElement: itemsArray){
            JsonObject itemObject = itemElement.getAsJsonObject();
            String name = itemObject.get("name").getAsString();
            String url = itemObject.getAsJsonObject("external_urls").get("spotify").getAsString();
            playlistItemList.add(new PlaylistItem(name,url));
        }
        return playlistItemList;
    }




    public JsonObject callApi(String apiString) throws InterruptedException, IOException{
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiServerPath + "/v1/browse/" + apiString))
                .GET()
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return JsonParser.parseString(response.body()).getAsJsonObject();
    }
    private void storeAccessToken(String responseBody){
        JsonObject responseBodyInJson = JsonParser.parseString(responseBody).getAsJsonObject();
        accessToken = responseBodyInJson.get("access_token").getAsString();
    }
    public boolean getShouldRun(){
        return shouldRun;
    }

    public String getCode() {
        return code;
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> queryPairs = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            try {
                String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                queryPairs.put(key, value);
            } catch (Exception e) {
                e.printStackTrace(); // Handle the exception as needed
            }
        }
        return queryPairs;
    }
    public void stopServer() throws IOException{
        System.out.println("about to stop server");
        server.stop(0);
        server = null;

    }
    public boolean isSiteHit(){
        return siteHit;
    }
    public void logSiteHit(){

        this.siteHit = true;
    }

}
