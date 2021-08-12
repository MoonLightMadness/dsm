package app.dsm.server.impl;


import app.dsm.server.BeatChecker;
import app.dsm.server.container.Container;
import app.dsm.server.container.ServerEntity;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import lombok.Data;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.ListIterator;

@Data
public class BeatCheckerImpl implements BeatChecker {

    private Container container;

    private long timeOut = 1000L;

    private int maxBeat = 50;

    private LogSystem log = LogSystemFactory.getLogSystem();

    @Override
    public void startBeat(Container container,long timeOut,int maxBeat) {
        this.container = container;
        this.timeOut = timeOut;
        this.maxBeat = maxBeat;
    }

    @Override
    public void run() {
        log.info("心跳检测模块开启中...");
        List<Container> list = container.getList();
        try {
            log.info("心跳检测模块开启完成,运行于线程:{}",Thread.currentThread().getName());
            while (true){
                ListIterator iter = list.listIterator();
                while (iter.hasNext()){
                    ServerEntity entity = (ServerEntity) iter.next();
                    if(entity.getBeat() == maxBeat){
                        log.info("服务器{}--{}离线,时间:{}",entity.getName(),entity.getSocketChannel().getRemoteAddress(), LocalTime.now());
                        entity.getSocketChannel().close();
                        iter.remove();
                    }
                    entity.setBeat(entity.getBeat()+1);
                }
                Thread.sleep(timeOut);
            }
        } catch (IOException | InterruptedException e) {
            log.error("心跳检测模块失效，原因：{}",e);
            e.printStackTrace();
        }
    }


}
