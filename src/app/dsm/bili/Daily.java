package app.dsm.bili;

import app.dsm.statemachine.EventManagerFactory;
import app.dsm.statemachine.impl.EventManager;

/**
 * @ClassName : app.dsm.bili.Daily
 * @Description :
 * @Date 2021-05-28 10:46:56
 * @Author ZhangHL
 */
public class Daily {
    private EventManager eventManager;

    private Controler controler;

    public void init(){
        eventManager= EventManagerFactory.getInstance();
        eventManager.setPackage("app.dsm.bili.event");
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
