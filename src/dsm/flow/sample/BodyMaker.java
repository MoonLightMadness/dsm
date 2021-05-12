package dsm.flow.sample;

import dsm.flow.ComponentMethod;
import org.junit.Test;

/**
 * @ClassName : dsm.flow.sample.BodyMaker
 * @Description :
 * @Date 2021-05-08 22:24:20
 * @Author 张怀栏
 */
public class BodyMaker extends ComponentMethod {
    @Override
    public boolean preRun() {
       return false;
    }

    @Override
    public void run() {
        Car c = (Car) getObj();

        c.setBody("body_A");
        this.setObj(c);
    }


}
