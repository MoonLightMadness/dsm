package app.dsm.novel;

import app.dsm.config.Configer;

public class BookShelf {

    private String configPath = null;

    public BookShelf(){
        Configer configer = new Configer();
        configPath = configer.readConfig("book_shelf");
    }

    public String getChapter(int index){

        return null;
    }

}
