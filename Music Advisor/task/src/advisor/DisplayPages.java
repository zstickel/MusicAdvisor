package advisor;

import java.util.List;
import java.util.Scanner;

public class DisplayPages <T>{
    List<T> listToDisplay;
    int numPerPage;
    int numPages;
    Scanner scanner;


    DisplayPages(List<T> itemList, int numPerPage, Scanner scanner){
        listToDisplay = itemList;
        this.numPerPage = numPerPage;
        if (listToDisplay.size() % numPerPage == 0){
            numPages = listToDisplay.size() / numPerPage;
        }else{
            numPages = (listToDisplay.size() / numPerPage) + 1;
        }
        this.scanner = scanner;
    }

    public int getNumPages(){
        return numPages;
    }
    public void printPages(int page){
        int startIndex = (page -1) * numPerPage;

        for(int i = startIndex; i< startIndex+numPerPage; i++){
            if (i >= listToDisplay.size()){
                break;
            }else{
                System.out.println(listToDisplay.get(i));
                System.out.println();
            }
        }
        System.out.println("---PAGE " + page + " OF " + numPages + "---");
    }


}
