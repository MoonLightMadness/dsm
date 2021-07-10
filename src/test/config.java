package test;

import app.dsm.config.Argument;
import app.dsm.config.ConfigReader;
import app.dsm.config.Configer;
import app.dsm.config.impl.IPConfigReader;
import org.junit.Test;

import java.io.File;

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
    
    @Test
    public void test2(){
        Configer configer = new Configer();
        configer.refreshLocal(new File("./metaconfig.txt"));
        System.out.println(configer.readConfig("test_property2"));
    }
    
    @Test
    public void test3(){
        String str = "asd -a aaa -b bbb -c ccc";
        System.out.println(Argument.getValue(str,"b"));
    }
}
