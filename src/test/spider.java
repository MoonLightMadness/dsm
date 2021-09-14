package test;

import app.dsm.bili.BiliSite;
import app.dsm.bili.Spider;
import app.dsm.collector.dataCollector.DataCollector;
import app.dsm.collector.dataCollector.domain.Target;
import app.dsm.config.Configer;
import app.utils.SimpleUtils;
import app.utils.net.WebDowloader;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName : test.spider
 * @Description :
 * @Date 2021-09-03 09:58:08
 * @Author ZhangHL
 */
public class spider {

    static Pattern pattern = Pattern.compile("<span(.*?)class=\"blog-text\"(.*?)>(.*?)</span>");


    public static String match(String data) {
        Matcher matcher = pattern.matcher(data);
        while (matcher.find()) {
            System.out.println(matcher.group(3));
        }
        return null;
    }

    @Test
    public void test() {
        try {
            String data = WebDowloader.downLoad("https://www.csdn.net/");
            match(data);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        Target target = new Target();
        target.setClassProperty("title");
        target.setMainProperty("a");
        DataCollector dataCollector = new DataCollector();
        List<String> res = dataCollector.getData(target, "https://www.csdn.net/");
        for (String s : res) {
            System.out.println(s);
        }
    }

    @Test
    public void test3(){
        BiliSite biliSite = new BiliSite();
        try {
            biliSite.setUrl(new URL("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/dynamic_new?uid=14910860&type_list=268435455&from=weball&platform=web"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Spider spider = new Spider(biliSite);
        String data = null;
        try {
            data = spider.downLoad();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //String data = new String(SimpleUtils.readFile("./test.txt"));
        Pattern card = Pattern.compile("\"desc\":(.*?),\"extend_json\"");
        Pattern name = Pattern.compile("uname\":\"(.*?)\",");
        Pattern title = Pattern.compile("\"title(.*?)\",");
        Pattern desc = Pattern.compile("description\\\\\":\\\\\"(.*?)\\\\\",");
        Pattern desc2 = Pattern.compile("ctime(.*?)desc(.*?)\"di");
        Pattern content = Pattern.compile("content(.*?),");
//        Matcher cm = cards.matcher(data);
//        data = cm.group(1);
        int count = 1;
        Matcher ccm = card.matcher(data);
        while (ccm.find()){
            System.out.println(count++);
            data = ccm.group(1);
            //System.out.println(data);
            Matcher md = name.matcher(data);
            Matcher mt = title.matcher(data);
            Matcher mdesc = desc.matcher(data);
            Matcher mdesc2 = desc2.matcher(data);
            Matcher mcontent = content.matcher(data);
            while (md.find()){
                System.out.println(md.group(1));
            }
            while (mt.find()){
                System.out.println("title:----"+mt.group(1));
            }
            while (mdesc.find()){
                System.out.println("description:----"+mdesc.group(1));
            }
            while (mdesc2.find()){
                System.out.println("desc:----"+mdesc2.group(2));
            }
            while (mcontent.find()){
                System.out.println("content:----"+mcontent.group(1));
            }
        }
    }

}
