package app.dsm.service.impl;

import app.dsm.base.BaseEntity;
import app.dsm.base.impl.UniversalEntity;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.dsm.service.entity.Consumer;
import app.utils.SimpleUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.ListIterator;

/**
 * @ClassName : app.dsm.mq.impl.MQSender
 * @Description :
 * @Date 2021-05-13 14:57:24
 * @Author ZhangHL
 */
public class ServiceSender implements Runnable {

    private List<BaseEntity> list;

    private LogSystem log;

    private List<Consumer> consumers;

    private long interval = 1;

    public void init(List<BaseEntity> list,List<Consumer> consumers, long interval) {
        this.list = list;
        this.consumers = consumers;
        log = LogSystemFactory.getLogSystem();
        this.interval = interval;
    }

    @Override
    public void run() {
        boolean isSend = false;
        try {
            while (true) {
                ListIterator<BaseEntity> iterator = list.listIterator();
                while (iterator.hasNext()) {
                    UniversalEntity uni = (UniversalEntity) iterator.next();
                    String type = uni.getMessageType();
                    ListIterator<Consumer> ci = consumers.listIterator();
                    while (ci.hasNext()) {
                        Consumer con = (Consumer) ci.next();
                        if (con.getChannel() == null) {
                            ci.remove();
                            continue;
                        }
                        if (con.getInterest().contains(type)) {
                            ServiceSender.send(con.getChannel(), SimpleUtils.serializableToBytes(uni));
                            isSend = true;
                        }
                    }
                    if (isSend) {
                        iterator.remove();
                        isSend = false;
                    }
                }
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void send(SocketChannel channel, byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.put(data);
        buffer.flip();
        try {
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
