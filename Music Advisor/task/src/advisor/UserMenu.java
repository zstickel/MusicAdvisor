package advisor;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class UserMenu{
    Scanner scanner = new Scanner(System.in);
    String accessServer;
    String apiServerPath;
    int numPages;
    UserMenu(String accessServer, String apiServerPath, int numPages){
        this.accessServer = accessServer;
        this.apiServerPath = apiServerPath;
        this.numPages = numPages;
    }
    public void mainMenu() throws IOException, InterruptedException{
        ServerManager serverManager = new ServerManager(accessServer, apiServerPath);
        boolean exit = false;
        boolean authenticated = false;
        String selection = scanner.nextLine();
        while(!exit){

            if (authenticated || selection.equals("exit") || selection.equals("auth")){
                String[] selectionArray = selection.split("\\s+");
                String playlistType = "";
                if (selectionArray.length > 1){
                    if (selectionArray[0].equals("playlists")){
                        selection = selectionArray[0];
                        for(int i=1; i<selectionArray.length; i++) {
                            playlistType += selectionArray[i];
                            playlistType += " ";
                        }
                        playlistType = playlistType.trim();
                    }
                }
                switch (selection) {
                    case "new":
                        //serverManager.printNew();
                        List<NewAlbumItem> newAlbumItemList = serverManager.getNewAlbumList();
                        selection = innerMenu(newAlbumItemList);
                        break;
                    case "featured":
                        //serverManager.printFeatured();
                        List<PlaylistItem> featuredAlbumList = serverManager.getFeatured();
                        selection = innerMenu(featuredAlbumList);
                        break;
                    case "categories":
                        //serverManager.printCategories();
                        List<String> categories = serverManager.getCategoriesList();
                        selection = innerMenu(categories);
                        break;
                    case "playlists":
                        //serverManager.printSelectedPlaylist(playlistType);
                        List<PlaylistItem> selectedPlaylist = serverManager.getSelectedPlaylist(playlistType);
                        if (selectedPlaylist == null){
                            selection = scanner.nextLine();
                        }else {
                            selection = innerMenu(selectedPlaylist);
                        }
                        break;
                    case "auth":
                        System.out.println("use this link to request access code:");
                        authenticated = true;
                        serverManager.runServer();
                        System.out.println(accessServer + "/authorize?client_id=a8cc81c8971c481d9328d3c660c9e625&redirect_uri=http://localhost:8080&response_type=code");
                        System.out.println("waiting for code...");
                        //while(serverManager.getShouldRun()) {
                            try {
                                Thread.sleep(1000); // Polling interval
                                System.out.println("Made it passed the wait");
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                System.err.println("Main thread interrupted");
                            }
                        //}
                        serverManager.stopServer();

                        System.out.println("code received");
                        System.out.println("Making http request for access_token...");
                        serverManager.requestSpotifyCode();
                        System.out.println("Success!");
                        selection = scanner.nextLine();
                        break;
                    case "exit":
                        exit = true;
                        System.out.println("---GOODBYE!---");
                        break;
                    default:
                        System.out.println("Invalid input");
                        selection = scanner.nextLine();
                }
            } else {
                    System.out.println("Please, provide access for application.");
                    selection = scanner.nextLine();
            }
        }
    }
    private <T> String  innerMenu(List<T> itemList) {

        int page = 1;
        DisplayPages<T> displayPages = new DisplayPages<>(itemList, numPages, scanner);
        displayPages.printPages(page);
        String selection = scanner.nextLine();
        boolean pageCommand = selection.equals("next") || selection.equals("prev");
        while (pageCommand) {
            if (selection.equals("prev")) {
                page--;
                if (page == 0) {
                    System.out.println("No more pages.");
                    page = 1;
                } else {
                    displayPages.printPages(page);
                }
            } else {
                page++;
                if (page > displayPages.getNumPages()) {
                    System.out.println("No more pages.");
                    page--;
                } else {
                    displayPages.printPages(page);
                }
            }
            selection = scanner.nextLine();
            pageCommand = selection.equals("next") || selection.equals("prev");
        }
        if (selection.equals("exit")){
            selection = scanner.nextLine();
        }
        return selection;
    }

}
