package app.dsm.flow.sample2;

import app.dsm.flow.sample2.Car;

import java.lang.reflect.InvocationTargetException;

public class CarMaker {

    public int makeBodyCheck(Object obj){
        return 1;
    }
    public int makeAdvanceBodyCheck(Object obj){
        return -1;
    }
    public int makeDoorCheck(Object obj){
        return 1;
    }
    public int makeWheelCheck(Object obj){
        return 1;
    }
    public void makeBody(Object obj) {
        ((Car)obj).setBody("body");
    }
    public void makeAdvanceBody(Object obj) {
        ((Car)obj).setBody("advancebody");
    }

    public void makeWheel(Object obj) {
        ((Car)obj).setWheel("wheel");
    }

    public void makeDoor(Object obj) throws IllegalAccessException {
            throw new IllegalAccessException("This is a test");
            //((Car)obj).setDoor("door");

       // int a = 3 / 0;
    }

}
