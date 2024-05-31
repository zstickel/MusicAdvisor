package advisor;

import java.util.ArrayList;
import java.util.List;

public class Categories implements MenuList {
    List<String> categories = new ArrayList<>();
    String header = "---CATEGORIES---";
    Categories(){
        categories.add("Top Lists");
        categories.add("Pop");
        categories.add("Mood");
        categories.add("Latin");
    }

    public List<String> getCategories() {
        return categories;
    }

    @Override
    public String getHeader() {
        return header;
    }

    @Override
    public List<String> getList() {
        return categories;
    }

    @Override
    public void printMenuList() {
        System.out.println(header);
        for(String item: categories){
            System.out.println(item);
        }
    }
}
