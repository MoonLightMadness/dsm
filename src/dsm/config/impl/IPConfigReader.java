package dsm.config.impl;

import dsm.config.ConfigReader;
import dsm.log.LogSystem;
import dsm.log.LogSystemFactory;
import dsm.utils.SimpleUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

/**
 * 关于IP的配置读取器
 * @ClassName : dsm.config.impl.IPConfigReader
 * @Description :
 * @Date 2021-05-01 20:51:05
 * @Author 张怀栏
 */
public class IPConfigReader implements ConfigReader {
    private static final String path="./config.txt";

    LogSystem log = LogSystemFactory.getLogSystem();
    @Override
    public String[] read() {
        String[] res=new String[2];
        try {
            BufferedReader br=new BufferedReader(new FileReader(new File(path)));
            String temp;
            while ((temp= br.readLine())!=null){
                temp=temp.toLowerCase(Locale.ROOT);
                temp=temp.replaceAll(" ","\0");
                if(temp.startsWith("ip=")){
                    res[0]=temp.substring(3);
                }
                if(temp.startsWith("port=")){
                    res[1]=temp.substring(5);
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
