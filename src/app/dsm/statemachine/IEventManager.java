package app.dsm.statemachine;

public interface IEventManager {
    /**
     * 设置包名
     *
     * @param packageName 包名
     */
    void setPackage(String packageName);

    /**
     * 激活事件
     *
     * @param eventName 事件名称
     * @param obj       obj
     * @return int
     */
    int activateEvent(String eventName,Object obj);


}
