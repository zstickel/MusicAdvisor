package advisor;

public class PlaylistItem {
    String name;
    String url;
    PlaylistItem(String name, String url){
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
    @Override public String toString(){
        return name + "\n" + url;
    }
}
