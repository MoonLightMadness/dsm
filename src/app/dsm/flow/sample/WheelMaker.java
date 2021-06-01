package app.dsm.flow.sample;

import app.dsm.flow.ComponentMethod;

import java.util.Map;

/**
 * @ClassName : app.dsm.flow.sample.WheelMaker
 * @Description :
 * @Date 2021-05-08 22:24:41
 * @Author ZhangHL
 */
public class WheelMaker extends ComponentMethod {
    @Override
    public boolean preRun() {
        return true;
    }

    @Override
    public void run() {
        Map map = getAttachment();
        Car c = (Car) map.get("car");
        c.setWheel("Wheel_A");
        //this.setAttachment(map);
    }
}
