package dsm.flow.sample;

import dsm.flow.FlowEngine;
import dsm.flow.FlowEngineUtil;
import dsm.flow.FlowUtils;
import dsm.flow.constant.FlowStatusEnum;
import dsm.log.debuger.Debuger;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName : dsm.flow.sample.Car
 * @Description :
 * @Date 2021-05-08 20:34:56
 * @Author ZhangHL
 */
public class Car {
    private String body;

    private String door;

    private String wheel;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDoor() {
        return door;
    }

    public void setDoor(String door) {
        this.door = door;
    }

    public String getWheel() {
        return wheel;
    }

    public void setWheel(String wheel) {
        this.wheel = wheel;
    }

    @Test
    public void test1(){
        Car c =new Car();
//            FlowEngine engine = FlowUtils.getFlowEngine();
//            String id = engine.startFlow("test_car_make",c);
//            while (!engine.getFlowStatus(id).equals(FlowStatusEnum.CLOSED.getMessage())){
//                Thread.sleep(1);
//            }
//            System.out.println(Debuger.getDebug());
        Map<String,Object> map = new HashMap<>();
        map.put("car",c);
        FlowEngineUtil.getAttachment("test_car_make",map);
        System.out.println(c.body);
        System.out.println(c.door);
        System.out.println(c.wheel);

    }
}
