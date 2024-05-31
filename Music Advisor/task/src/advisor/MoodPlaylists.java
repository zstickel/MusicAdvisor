package advisor;

import java.util.ArrayList;
import java.util.List;

public class MoodPlaylists implements MenuList {
    List<String> moodPlaylist = new ArrayList<>();
    String header = "---MOOD PLAYLISTS---";
    MoodPlaylists(){
        moodPlaylist.add("Walk Like A Badass");
        moodPlaylist.add("Rage Beats");
        moodPlaylist.add("Arab Mood Booster");
        moodPlaylist.add("Sunday Stroll");
    }

    @Override
    public String getHeader() {
        return header;
    }

    @Override
    public List<String> getList() {
        return moodPlaylist;
    }

    @Override
    public void printMenuList() {
        System.out.println(header);
        for(String item: moodPlaylist){
            System.out.println(item);
        }
    }
}
