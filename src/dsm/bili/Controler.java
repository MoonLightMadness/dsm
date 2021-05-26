package dsm.bili;

import dsm.utils.SimpleUtils;
import dsm.utils.TimeFormatter;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Controler {
    /**
     * 检查日期
     *
     * @return boolean 小于一天返回true，否则返回false
     * @throws SQLException           sqlexception异常
     * @throws ClassNotFoundException 类没有发现异常
     */
    public boolean checkDate() throws SQLException, ClassNotFoundException {
        DBforBili dbb = new DBforBili();
        String last = dbb.getDateTime();
        String currentDate = SimpleUtils.getTimeStamp2(TimeFormatter.DAY_LEVEL);
        dbb.close();
        return last.equals(currentDate);
    }

    public String getStatus() throws SQLException, ClassNotFoundException {
        DBforBili dbb = new DBforBili();
        String s = dbb.getStatus();
        dbb.close();
        return s;
    }

    public static void getRoutine() throws SQLException, ClassNotFoundException, IOException {
        Controler controler = new Controler();
        DBforBili dbb = new DBforBili();
        if (!controler.checkDate()) {
            BiliSite bili = new BiliSite();
            bili.setUrl(new URL("https://www.bilibili.com/v/popular/rank/all"));
            Spider spider = new Spider(bili);
            Extractor extractor = new Extractor(spider.downLoad());
            String[] title = extractor.getTitle().split("\n");
            String[] author = extractor.getAuthor().split("\n");
            String[] point = extractor.getPoint().split("\n");
            String[] url = extractor.getUrl().split("\n");
            for (int i = 0; i < title.length; i++) {
                dbb.writeWithoutTags(url[i], author[i], point[i], title[i], new Date(new java.util.Date().getTime()).toString());
            }
            dbb.turnToUndone();
            dbb.updateDateTime(new Date(new java.util.Date().getTime()).toString());

        }
        dbb.close();
    }

    public static void getTags() throws SQLException, ClassNotFoundException, ParseException, IOException, InterruptedException {
        BiliSite bili = new BiliSite();
        Spider spider;
        Extractor extractor = new Extractor("c");
        DBforBili dbb = new DBforBili();
        List<String> list = dbb.readToay();
        try {
            for (String text : list) {
                if(!SimpleUtils.isEmptyString(text)){
                    bili.setUrl(new URL(text));
                    spider = new Spider(bili);
                    String data = spider.downLoad();
                    String res = extractor.getTags(data);
                    System.out.println(text+"  "+res);
                    dbb.writeTodayTags(text,res);
                    Thread.sleep(1000);
                }
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        dbb.setToDone();
        dbb.close();
        System.out.println("Done");
    }
}
