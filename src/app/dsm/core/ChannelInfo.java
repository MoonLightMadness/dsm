package app.dsm.core;

import app.dsm.log.LogSystem;
import app.dsm.log.LogSystemFactory;
import app.dsm.utils.SimpleUtils;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * @ClassName : app.dsm.core.EntityInfo
 * @Description :
 * @Date 2021-05-17 09:15:44
 * @Author ZhangHL
 */
public class ChannelInfo {

    private SocketChannel channel;

    private String name;

    private String ip;

    private LogSystem log = LogSystemFactory.getLogSystem();

    /**
     * 心跳，超过一定次数则认为死亡
     */
    private int beat = 0;

    private String stime = SimpleUtils.getTimeStamp();

    private String lastCheck = stime;

    /**
     * 心跳加一
     */
    public void beat() {
        beat += 1;
        if (beat > 20) {
            try {
                log.info(null,"{}({})--Offline at:{}",name,channel.getRemoteAddress().toString(),SimpleUtils.getTimeStamp());
            } catch (IOException e) {
                try {
                    log.error(null, "{}:--{}",channel.getRemoteAddress().toString(),e.getMessage());
                } catch (IOException ioException) {
                    log.error(null, "{}",ioException.getMessage());
                }
            }
            //channel = null;
        }
    }

    /**
     * 重置心跳计数
     */
    public void reset() {
        beat = 0;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
