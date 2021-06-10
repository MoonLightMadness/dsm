package test;

import app.dsm.flow.FlowChain;
import app.dsm.flow.FlowEngineX;
import app.dsm.flow.sample.Car;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class flow {

    @Test
    public void test1(){
        String path = "./flows/test_car_make/test_car_make.yaml";
        File f =new File(path);
        Yaml yaml = new Yaml();
        try {
            Map<String,String> flow = yaml.load(new FileInputStream(f));
            Iterator<Map.Entry<String,String>> iterator = flow.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry values = iterator.next();
                Map sub =(Map)values.getValue();
                Iterator i1 = sub.entrySet().iterator();
                while (i1.hasNext()){
                    Map.Entry e = (Map.Entry)i1.next();
                    System.out.println(e.getKey()+" "+e.getValue());
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test2(){
        FlowEngineX fex = new FlowEngineX();
        FlowChain fc = fex.getChainByName("test_car_make");
        Car c1 = new Car();
        fc.start(c1);
        System.out.println(c1.getBody());
        System.out.println(c1.getDoor());
        System.out.println(c1.getWheel());
    }
}
