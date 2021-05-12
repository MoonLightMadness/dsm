package dsm.flow.sample;

import dsm.flow.ComponentMethod;

/**
 * @ClassName : dsm.flow.sample.AdvancedBodyMaker
 * @Description :
 * @Date 2021-05-09 13:41:18
 * @Author ZhangHL
 */
public class AdvancedBodyMaker extends ComponentMethod {
    @Override
    public boolean preRun() {
        return true;
    }

    @Override
    public void run() {


        Car c =(Car) getObj();

        c.setBody("advanced_body_A");

        setObj(c);


    }
}
