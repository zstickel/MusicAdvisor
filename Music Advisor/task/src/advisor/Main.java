package advisor;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String accessValue = null;
        String apiServerPath = null;
        int numPerPage = 5;

        // Iterate through the command-line arguments
        for (int i = 0; i < args.length; i++) {
            if ("-access".equals(args[i]) && i + 1 < args.length) {
                accessValue = args[i + 1];
            }
            if("-resource".equals(args[i]) && i+1 < args.length){
                apiServerPath = args[i+1];
            }
            if("-page".equals(args[i]) && i+1 < args.length){
                numPerPage = Integer.parseInt(args[i+1]);
            }
        }

        if (accessValue == null){
            accessValue = "https://accounts.spotify.com";
        }
        if(apiServerPath == null){
            apiServerPath = "https://api.spotify.com";
        }
        UserMenu userMenu = new UserMenu(accessValue, apiServerPath, numPerPage);
        userMenu.mainMenu();
    }
}
