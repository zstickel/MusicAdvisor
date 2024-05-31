package advisor;

import java.util.ArrayList;
import java.util.List;

public class NewReleases implements MenuList {
    List<String> newReleases = new ArrayList<>();
    String header = "---NEW RELEASES---";
    NewReleases(){
        newReleases.add("Mountains [Sia, Diplo, Labrinth]");
        newReleases.add("Runaway [Lil Peep]");
        newReleases.add("The Greatest Show [Panic! At the Disco]");
        newReleases.add("All Out Life [Slipknot]");
    }

    public List<String> getNewReleases() {
        return newReleases;
    }

    @Override
    public String getHeader() {
        return header;
    }

    @Override
    public List<String> getList() {
        return newReleases;
    }

    @Override
    public void printMenuList() {
        System.out.println(header);
        for(String item: newReleases){
            System.out.println(item);
        }
    }
}
