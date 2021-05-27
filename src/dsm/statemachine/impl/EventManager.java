package dsm.statemachine.impl;

import dsm.log.LogSystem;
import dsm.log.LogSystemFactory;
import dsm.statemachine.HistoryEntityConstructor;
import dsm.statemachine.IEventManager;
import dsm.statemachine.entity.HistoryEntity;
import dsm.utils.SimpleUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件管理器
 *
 * @author ZhangHL
 * @version 1.0.0
 * @date 2021/05/27
 * @see IEventManager
 */
public class EventManager implements IEventManager {

    private String packageName;

    private LogSystem log = LogSystemFactory.getLogSystem();

    private List<HistoryEntity> history;
    /**
     * 设置包名
     *
     * @param packageName 包名
     */
    @Override
    public void setPackage(String packageName) {
        this.packageName = packageName;
    }

    /**
     * 激活事件
     *
     * @param eventName 事件名称
     * @param obj       obj
     * @return int 0-表示失败 1-运行成功 x-其他
     */
    @Override
    public int activateEvent(String eventName, Object obj) {
        String path = pathConstructor(eventName);
        try {
            String time = SimpleUtils.getTimeStamp();
            Event e = (Event) Class.forName(path).newInstance();
            int res = e.activateAction(obj);
            if(res == 0){
                log.info(null,"触发事件:{},返回失败结果",eventName);
            }
            //记录到历史事件当中
            addLog(time,eventName, res);
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

    private void initHistory(){
        history = new ArrayList<>();
    }

    public List<HistoryEntity> getHistory() {
        return history;
    }

    private void addLog(String time, String eventName, int res){
        if(history==null){
            initHistory();
        }
        HistoryEntity entity = HistoryEntityConstructor.getEntity(time,eventName, res);
        history.add(entity);
    }
}
