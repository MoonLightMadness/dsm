package dsm.core;

import dsm.base.BaseEntity;
import dsm.utils.SimpleUtils;

import java.nio.channels.SocketChannel;

/**
 * @ClassName : dsm.core.EntityInfo
 * @Description :
 * @Date 2021-05-17 09:15:44
 * @Author ZhangHL
 */
public class ChannelInfo {

    private SocketChannel channel;

    /**
     * 心跳，超过一定次数则认为死亡
     */
    private int beat = 0;

    private String stime = SimpleUtils.getTimeStamp();

    private String lastCheck = stime;

    /**
     * 时间间隔,默认单位:秒
     */
    private int interval;

    public SocketChannel getChannel() {
        return channel;
    }

    public void setChannel(SocketChannel channel) {
        this.channel = channel;
    }

    public int getBeat() {
        return beat;
    }

    public void setBeat(int beat) {
        this.beat = beat;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getLastCheck() {
        return lastCheck;
    }

    public void setLastCheck(String lastCheck) {
        this.lastCheck = lastCheck;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
