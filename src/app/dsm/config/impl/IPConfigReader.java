package app.dsm.config.impl;

import app.dsm.config.ConfigReader;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.utils.SimpleUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

/**
 * 关于IP的配置读取器
 *
 * @ClassName : app.dsm.config.impl.IPConfigReader
 * @Description :
 * @Date 2021-05-01 20:51:05
 * @Author ZhangHL
 */
public class IPConfigReader implements ConfigReader {
    private static final String path = "./config.txt";

    LogSystem log = LogSystemFactory.getLogSystem();

    @Override
    public String[] read() {
        String[] res = new String[2];
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path)));
            String temp;
            while ((temp = br.readLine()) != null) {
                temp = temp.toLowerCase(Locale.ROOT);
                temp = temp.replaceAll(" ", "\0");
                if (temp.startsWith("ip=")) {
                    res[0] = temp.substring(3);
                }
                if (temp.startsWith("port=")) {
                    res[1] = temp.substring(5);
                }
            }
            br.close();
        } catch (FileNotFoundException ffe) {
            log.info(this.getClass().getName(), "找不到配置文件");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (SimpleUtils.isEmptyString(res[0])) {
            try {
                res[0] = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public static String readPortByName(String name) {
        String res = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("./config.txt"));
            String temp;
            while ((temp = reader.readLine()) != null) {
                temp = temp.toLowerCase(Locale.ROOT);
                temp = temp.trim();
                if (temp.startsWith(name)) {
                    res = temp.split("=")[1];
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
