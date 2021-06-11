package test;

import app.dsm.flow.FlowChain;
import app.dsm.flow.FlowEngineX;
import app.dsm.flow.constant.FlowMode;
import app.dsm.flow.sample2.Car;
import app.utils.SimpleUtils;
import app.utils.special.RTimer;
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
        RTimer rTimer = new RTimer();
        rTimer.start();
        Car c1 = new Car();
        FlowEngineX fex = new FlowEngineX();
        String id = fex.startFlow("test_car_make", FlowMode.IK.getMessage(),c1);
        System.out.println(rTimer.end());
        rTimer.start();
        System.out.println(SimpleUtils.getTimeStamp());
        try {
            while (!fex.checkFlow(id)){
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(SimpleUtils.getTimeStamp());
        System.out.println(rTimer.end());
        System.out.println(c1.getBody());
        System.out.println(c1.getDoor());
        System.out.println(c1.getWheel());
    }
}
