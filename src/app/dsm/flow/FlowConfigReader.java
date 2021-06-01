package app.dsm.flow;

import app.dsm.config.ConfigReader;
import app.dsm.log.LogSystem;
import app.dsm.log.LogSystemFactory;
import app.utils.SimpleUtils;

import java.io.*;
import java.util.Locale;

/**
 * @ClassName : app.dsm.flow.FlowConfigReader
 * @Description :
 * @Date 2021-05-08 16:48:27
 * @Author ZhangHL
 */
public class FlowConfigReader implements ConfigReader {

    private final LogSystem log = LogSystemFactory.getLogSystem();

    @Override
    public String[] read() {
        String[] res = new String[3];
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("./flow.txt")));
            String temp;
            while ((temp = br.readLine()) != null) {
                temp = temp.toLowerCase(Locale.ROOT);
                temp = temp.replaceAll(" ", "\0");
                if (temp.startsWith("flow_core_size")) {
                    res[0] = temp.substring("flow_core_size=".length());
                }else if(temp.startsWith("max_thread")){
                    res[1]=temp.substring("max_thread=".length());
                }else if(temp.startsWith("keep_alive")){
                    res[2]=temp.substring("keep_alive=".length());
                }
            }
            br.close();
        } catch (FileNotFoundException ffe) {
            log.error(this.getClass().getName(), "找不到线程池配置文件,切换到默认配置");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(SimpleUtils.isEmptyString(res[0])){
            res[0]="100";
        }
        if(SimpleUtils.isEmptyString(res[1])){
            res[1]="200";
        }
        if(SimpleUtils.isEmptyString(res[2])){
            res[2]="300";
        }
        return res;
    }
}
