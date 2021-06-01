package app.dsm.statemachine.entity;

/**
 * 事件触发历史实体
 *
 * @author ZhangHL
 * @version 1.0.0
 * @date 2021/05/27
 */
public class HistoryEntity {

    private String timestamp;

    private String eventName;

    private int res = -1;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
