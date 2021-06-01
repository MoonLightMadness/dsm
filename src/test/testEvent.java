package test;

import app.dsm.statemachine.EventManagerFactory;
import app.dsm.statemachine.entity.HistoryEntity;
import app.dsm.statemachine.impl.Event;
import app.dsm.statemachine.impl.EventManager;
import org.junit.Test;

public class testEvent extends Event {

    private int atk =100;

    @Override
    public int activateAction(Object obj) {
        System.out.println("I'm active");
        testEvent t = (testEvent)obj;
        t.atk *= (1.1f);
        System.out.println(t.atk);
        return 1;
    }

    @Test
    public void test1(){
        EventManager manager = EventManagerFactory.getInstance();
        manager.setPackage("test");
        manager.activateEvent("db",this);
        manager.activateEvent("testEvent",this);
        manager.activateEvent("testEvent",this);
        manager.activateEvent("testEvent",this);
        manager.activateEvent("testEvent",this);
        for(HistoryEntity entity: manager.getHistory()){
            System.out.println(entity.getTimestamp()+"  "+entity.getEventName()+"  "+entity.getRes());
        }
    }
}
