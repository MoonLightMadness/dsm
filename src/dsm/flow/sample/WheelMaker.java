package dsm.flow.sample;

import dsm.flow.ComponentMethod;

/**
 * @ClassName : dsm.flow.sample.WheelMaker
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
        Car c = (Car) getObj();

        c.setWheel("Wheel_A");

        this.setObj(c);
    }
}
