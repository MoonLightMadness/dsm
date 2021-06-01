package app.dsm.bili;

public class Extracted {
    public String url;
    public String author;
    public String point;
    public String title;
    public String tags;
    public Extracted(){}
    public Extracted(String url,String author,String point,String title,String tags){
        this.author=author;
        this.point=point;
        this.tags=tags;
        this.title=title;
        this.url=url;
    }
}
