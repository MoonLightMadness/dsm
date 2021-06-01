package app.dsm.config.impl;

import app.dsm.config.ConfigReader;
import app.dsm.log.LogSystem;
import app.dsm.log.LogSystemFactory;
import app.dsm.utils.SimpleUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

/**
 * @ClassName : app.dsm.config.impl.DatabaseConfigReader
 * @Description :
 * @Date 2021-05-04 14:17:58
 * @Author ZhangHL
 */
public class DatabaseConfigReader implements ConfigReader {
    private static final String path="./config.txt";
    private final LogSystem log = LogSystemFactory.getLogSystem();
    @Override
    public String[] read() {
        String[] res=new String[1];
        try {
            BufferedReader br=new BufferedReader(new FileReader(new File(path)));
            String temp;
            while ((temp= br.readLine())!=null){
                temp=temp.toLowerCase(Locale.ROOT);
                temp=temp.replaceAll(" ","\0");
                if(temp.startsWith("database=")){
                    res[0]=temp.substring("database=".length());
                }
            }
            br.close();
        }catch (FileNotFoundException ffe){
            log.info(this.getClass().getName(),"找不到配置文件");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(SimpleUtils.isEmptyString(res[0])){
            try {
                res[0]= InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
}
