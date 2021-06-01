package app.dsm.flow.sample;

import app.dsm.flow.ComponentMethod;

import java.util.Map;

/**
 * @ClassName : app.dsm.flow.sample.BodyMaker
 * @Description :
 * @Date 2021-05-08 22:24:20
 * @Author ZhangHL
 */
public class BodyMaker extends ComponentMethod {
    @Override
    public boolean preRun() {
       return false;
    }

    @Override
    public void run() {
        Map map = getAttachment();
        Car c = (Car) map.get("car");
        c.setBody("Body_A");
        //this.setAttachment(map);
    }


}
