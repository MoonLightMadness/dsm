package test;

import app.dsm.collector.dataCollector.DataCollector;
import app.dsm.collector.dataCollector.domain.Target;
import app.utils.net.WebDowloader;
import org.junit.Test;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.List;
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

}
