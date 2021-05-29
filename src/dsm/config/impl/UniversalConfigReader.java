package dsm.config.impl;

import dsm.config.ConfigReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Locale;

public class UniversalConfigReader implements ConfigReader {

    private String name;

    @Override
    public String[] read() {
        String file = "./config.txt";
        String ip = null, port = null;
        String[] res = new String[2];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String temp;
            while ((temp = reader.readLine()) != null) {
                temp = temp.toLowerCase(Locale.ROOT);
                temp = temp.replaceAll(" ", "");
                if (temp.startsWith(name+".ip=")) {
                    ip = temp.substring((name+".ip=").length());
                }
                if (temp.startsWith(name+".port")) {
                    port = temp.substring((name+".port=").length());
                }
            }
            if (ip == null) {
                ip = InetAddress.getLocalHost().getHostAddress();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        res[0] = ip;
        res[1] = port;
        return res;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
