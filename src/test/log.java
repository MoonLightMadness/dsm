package test;

import app.dsm.base.JSONTool;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.utils.SimpleUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName : test.log
 * @Description :
 * @Date 2021-05-01 21:06:45
 * @Author ZhangHL
 */
public class log {
    @Test
    public void logTest1(){
        LogSystem log= LogSystemFactory.getLogSystem();
        log.immediatelySaveMode(true);
        log.info(this.getClass().getName(),"this is a test");
    }

    @Test
    public void test2(){
        Car audi = new Car();
        audi.name = "audi";
        Factory factory = new Factory();
        factory.name = "fac";
        factory.c = audi;
        SimpleUtils.writeFile("./test.txt",SimpleUtils.serializableToBytes(factory));
    }

    @Test
    public void test3(){
        Factory factory = (Factory) SimpleUtils.bytesToSerializableObject(SimpleUtils.readFile("./test.txt"));
        System.out.println(factory.name);
        System.out.println(factory.c.name);
    }

    @Test
    public void test4(){
        Car audi = new Car();
        audi.name = "audi";
        Factory factory = new Factory();
        factory.name = "fac";
        factory.c = audi;
        System.out.println(JSON.toJSONString(factory));
    }
}
class Factory implements Serializable {
    public String name;
    public Car c;
}
class Car implements Serializable{
    public String name;
}