package dsm.bili;

import dsm.statemachine.EventManagerFactory;
import dsm.statemachine.impl.EventManager;

import javax.sound.sampled.Control;

/**
 * @ClassName : dsm.bili.Daily
 * @Description :
 * @Date 2021-05-28 10:46:56
 * @Author ZhangHL
 */
public class Daily {
    private EventManager eventManager;

    private Controler controler;

    public void init(){
        eventManager= EventManagerFactory.getInstance();
        eventManager.setPackage("dsm.bili.event");
        controler = new Controler();
    }

    public int updateData(){
        int res = eventManager.activateEvent("UpdateEvent",controler);
        return res;
    }

    public int updateTags(){
        int res = eventManager.activateEvent("UpdateTagEvent",controler);
        return res;
    }

    public int showNew(){
        int res = eventManager.activateEvent("ShowNewEvent",controler);
        return res;
    }

}
