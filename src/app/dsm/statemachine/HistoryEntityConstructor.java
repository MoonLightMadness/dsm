package app.dsm.statemachine;

import app.dsm.statemachine.entity.HistoryEntity;

public class HistoryEntityConstructor {

    public static HistoryEntity getEntity(String time,String eventName, int res){
        HistoryEntity entity = new HistoryEntity();
        entity.setTimestamp(time);
        entity.setEventName(eventName);
        entity.setRes(res);
        return entity;
    }

    public static HistoryEntity getEntity(String time,String eventName){
        HistoryEntity entity = new HistoryEntity();
        entity.setTimestamp(time);
        entity.setEventName(eventName);
        return entity;
    }
}
