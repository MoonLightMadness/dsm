package dsm.statemachine.impl;

import dsm.log.LogSystem;
import dsm.log.LogSystemFactory;
import dsm.statemachine.IEventManager;
import dsm.utils.SimpleUtils;

public class EventManager implements IEventManager {

    private String packageName;

    private LogSystem log = LogSystemFactory.getLogSystem();

    @Override
    public void setPackage(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public int activateEvent(String eventName, Object obj) {
        String path = pathConstructor(eventName);
        try {
            Event e = (Event) Class.forName(path).newInstance();
            int res = e.activateAction(obj);
            return res;
        } catch (ClassNotFoundException e) {
            log.error(null,"未找到该类") ;
        } catch (InstantiationException e) {
            log.error(null,"类初始化失败") ;
        } catch (IllegalAccessException e) {
            log.error(null,"非法错误") ;
        }
        return 0;
    }

    private String pathConstructor(String eventName){
        StringBuilder sb = new StringBuilder();
        if(SimpleUtils.isEmptyString(packageName)){
            return sb.toString();
        }
        sb.append(packageName).append(".").append(eventName);
        return sb.toString();
    }
}
