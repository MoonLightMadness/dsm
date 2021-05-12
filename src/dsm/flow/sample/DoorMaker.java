package dsm.flow.sample;

import dsm.flow.ComponentMethod;

/**
 * @ClassName : dsm.flow.sample.DoorMaker
 * @Description :
 * @Date 2021-05-08 22:24:31
 * @Author 张怀栏
 */
public class DoorMaker extends ComponentMethod {
    @Override
    public boolean preRun() {
        return true;
    }

    @Override
    public void run() {
        Car c = (Car) getObj();

        c.setDoor("Door_A");

        this.setObj(c);
    }
}
