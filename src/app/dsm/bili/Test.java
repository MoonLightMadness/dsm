package app.dsm.bili;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class Test {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DBforBili dbb=new DBforBili();
        List<Extracted> t=dbb.read(new Date(new java.util.Date().getTime()));
        for(int i=0;i<t.size();i++){
            System.out.println(t.get(i).tags);
        }
    }

    @org.junit.Test
    public void test1() throws IOException {
        BiliSite bili=new BiliSite();
        bili.setUrl(new URL("https://www.bilibili.com/v/popular/rank/all"));
        Spider spider=new Spider(bili);
        String res = spider.downLoad();
        //System.out.println(res);
        Extractor extractor=new Extractor(res);
        System.out.println(extractor.getTitle().split("\n").length);
        System.out.println(extractor.getAuthor());
        System.out.println(extractor.getPoint().split("\n").length);
        System.out.println(extractor.getUrl().split("\n").length);
    }
}
