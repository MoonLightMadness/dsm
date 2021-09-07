package app.dsm.collector.dataCollector;

import app.dsm.collector.dataCollector.builder.DataCollectorRegexBuilder;
import app.dsm.collector.dataCollector.domain.Target;
import app.utils.net.WebDowloader;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName : app.dsm.collector.dataCollector.DataCollector
 * @Description :
 * @Date 2021-09-03 10:57:40
 * @Author ZhangHL
 */
public class DataCollector {


    public List<String> getData(Target target,String url){
        try {
            String data = WebDowloader.downLoad(url);
            DataCollectorRegexBuilder dataCollectorRegexBuilder = new DataCollectorRegexBuilder(target);
            return search(data,dataCollectorRegexBuilder.toString());
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> search(String data, String patternStr){
        List<String> res = new ArrayList<>();
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(data);
        while (matcher.find()){
            res.add(matcher.group(3));
        }
        return res;
    }


}
