package app.dsm.flow.vo;

import app.dsm.flow.FlowChain;
import app.utils.SimpleUtils;

public class FlowTaskEntity {

    private String id;

    private FlowChain fc;

    private String date;

    private boolean isFinished;

    public FlowTaskEntity(String id,FlowChain fc){
        this.id = id;
        this.fc = fc;
        date = SimpleUtils.getTimeStamp();
        isFinished = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FlowChain getFc() {
        return fc;
    }

    public void setFc(FlowChain fc) {
        this.fc = fc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
