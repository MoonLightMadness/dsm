package dsm.bili;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Controler {
    public boolean checkDate() throws SQLException, ClassNotFoundException {
        DBforBili dbb=new DBforBili();
        Date nowTime=new Date(new java.util.Date().getTime());
        Date lastTime=Date.valueOf(dbb.getDateTime());
        dbb.close();
        return lastTime.toString().equals(nowTime.toString());
    }
    public String getStatus() throws SQLException, ClassNotFoundException {
        DBforBili dbb=new DBforBili();
        String s=dbb.getStatus();
        dbb.close();
        return s;
    }
    public static void getRoutine() throws SQLException, ClassNotFoundException, IOException {
        Controler controler=new Controler();
        DBforBili dbb=new DBforBili();
        if(!controler.checkDate() ) {
            BiliSite bili=new BiliSite();
            bili.setUrl(new URL("https://www.bilibili.com/v/popular/rank/all"));
            Spider spider=new Spider(bili);
            Extractor extractor=new Extractor(spider.downLoad());
            String[] title=extractor.getTitle().split("\n");
            String[] author=extractor.getAuthor().split("\n");
            String[] point=extractor.getPoint().split("\n");
            String[] url=extractor.getUrl().split("\n");
            for(int i=0;i<title.length;i++){
                dbb.writeWithoutTags(url[i],author[i],point[i],title[i],new Date(new java.util.Date().getTime()).toString());
            }
            dbb.turnToUndone();
            dbb.updateDateTime(new Date(new java.util.Date().getTime()).toString());

        }
        dbb.close();
    }
    public static void getTags() throws SQLException, ClassNotFoundException, ParseException, IOException, InterruptedException {
        BiliSite bili=new BiliSite();
        Spider spider;
        Extractor extractor=new Extractor("c");
        DBforBili dbb=new DBforBili();
        String nowDay=new Date(new java.util.Date().getTime()).toString();
        List<Extracted> afterDay=dbb.read(Tool.ChangeDate(new Date(new java.util.Date().getTime()),-1));
        if(afterDay.size()==0){
            for(int i=2;i<8;i++){
                afterDay=dbb.read(Tool.ChangeDate(new Date(new java.util.Date().getTime()),-i));
                if(afterDay.size()!=0){
                    break;
                }
                if(i==7){
                    System.out.println("No more data");
                    System.exit(0);
                }
            }
        }
        List<Extracted> thisDay=dbb.read(Tool.ChangeDate(new Date(new java.util.Date().getTime()),0));
        List<Extracted> newVideo=new ArrayList<>();
        boolean isRepeat=false;
        for (Extracted value : thisDay) {
            for (Extracted extracted : afterDay) {
                if (value.title.equals(extracted.title)) {
                    System.out.println("Found "+value.title);
                    dbb.writeTags(value.title, extracted.tags, nowDay);
                    isRepeat=true;
                    break;
                }
            }
            if(!isRepeat) {
                newVideo.add(value);
            }
            isRepeat=false;
        }
        int count=1;
        for(int i=0;i<newVideo.size();i++){
            bili.setUrl(new URL(newVideo.get(i).url));
            spider=new Spider(bili);
            dbb.writeTags(newVideo.get(i).title,extractor.getTags(spider.downLoad()),nowDay);
            System.out.print("\rProcessing:"+String.valueOf(count)+"/"+String.valueOf(newVideo.size())+" "+newVideo.get(i).title);
            count++;
            Thread.sleep(5000);
        }
        dbb.setToDone();
        dbb.close();
        System.out.println("Done");
    }
}
