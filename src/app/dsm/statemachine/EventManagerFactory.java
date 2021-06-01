package app.dsm.statemachine;

import app.dsm.statemachine.impl.EventManager;

/**
 * 事件管理器工厂
 *
 * @author ZhangHL
 * @version 1.0.0
 * @date 2021/05/27
 */
public class EventManagerFactory {

    /**
     * 获得实例
     *
     * @return {@link EventManager}
     */
    public static EventManager getInstance(){
        return new EventManager();
    }
}
