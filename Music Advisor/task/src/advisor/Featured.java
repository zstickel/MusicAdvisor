package advisor;

import java.util.ArrayList;
import java.util.List;

public class Featured implements MenuList{
    List<String> featured = new ArrayList<>();
    String header = "---FEATURED---";
    Featured(){
        featured.add("Mellow Morning");
        featured.add("Wake Up and Smell the Coffee");
        featured.add("Monday Motivation");
        featured.add("Songs to Sing in the Shower");
    }

    @Override
    public String getHeader() {
        return header;
    }

    @Override
    public List<String> getList() {
        return featured;
    }

    @Override
    public void printMenuList() {
        System.out.println(header);
        for(String item : featured){
            System.out.println(item);
        }
    }
}
