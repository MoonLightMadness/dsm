package test;

import dsm.statemachine.impl.Event;
import dsm.statemachine.impl.EventManager;
import org.junit.Test;

public class testEvent extends Event {
    @Override
    public int activateAction(Object obj) {
        System.out.println("I'm active");
        System.out.println((int) obj +1);
        return 1;
    }

    @Test
    public void test1(){
        EventManager manager = new EventManager();
        manager.setPackage("test");
        int i =1;
        manager.activateEvent("testEvent",i);
    }
}
