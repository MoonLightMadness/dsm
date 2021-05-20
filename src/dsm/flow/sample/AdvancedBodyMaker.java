package dsm.flow.sample;

import dsm.flow.ComponentMethod;

import java.util.Map;

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


        Map map = getAttachment();
        Car c = (Car) map.get("car");
        c.setBody("Adv_Body_A");
        //this.setAttachment(map);


    }
}
