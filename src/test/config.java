package test;

import app.dsm.config.ConfigReader;
import app.dsm.config.impl.IPConfigReader;
import org.junit.Test;

/**
 * @ClassName : test.config
 * @Description :
 * @Date 2021-05-01 21:09:49
 * @Author ZhangHL
 */
public class config {
    @Test
    public void ipConfigReaderTest1(){
        ConfigReader reader = new IPConfigReader();
        for(String s : reader.read()){
            System.out.println(s);
        }
    }
}
