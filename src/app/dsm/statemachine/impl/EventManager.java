package app.dsm.statemachine.impl;

import app.dsm.log.LogSystem;
import app.dsm.log.LogSystemFactory;
import app.dsm.statemachine.HistoryEntityConstructor;
import app.dsm.statemachine.IEventManager;
import app.dsm.statemachine.entity.HistoryEntity;
import app.dsm.utils.SimpleUtils;

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
     * @return int 0-表示失败 1-运行成功 -1-发生未知错误
     */
    @Override
    public int activateEvent(String eventName, Object obj) {
        String path = pathConstructor(eventName);
        try {
            String time = SimpleUtils.getTimeStamp();
            HistoryEntity entity = HistoryEntityConstructor.getEntity(time,eventName);
            addLog(entity);
            Event e = (Event) Class.forName(path).newInstance();
            int res = e.activateAction(obj);
            if(res == 0){
                log.info(null,"触发事件:{},返回失败结果",eventName);
            }
            //记录到历史事件当中
            updateHistory(entity,res);
            return res;
        } catch (ClassNotFoundException e) {
            log.error(null,"未找到该类") ;
        } catch (InstantiationException e) {
            log.error(null,"类初始化失败") ;
        } catch (IllegalAccessException e) {
            log.error(null,"非法错误") ;
        } catch (ClassCastException e){
            log.error(null, "{}必须是Event的子类",eventName);
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

    private void addLog(HistoryEntity entity){
        if(history==null){
            initHistory();
        }
        history.add(entity);
    }

    private void updateHistory(HistoryEntity entity,int res){
        entity.setRes(res);
    }
}
