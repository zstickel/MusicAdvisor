package advisor;

import java.util.List;

public class NewAlbumItem {
    String name;
    List<String> artists;
    String url;
    NewAlbumItem(String name, List<String> artists, String url){
        this.name = name;
        this.artists = artists;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getArtists() {
        return artists;
    }
    @Override public String toString(){
        return name + "\n" + artists + "\n" + url;
    }
}
