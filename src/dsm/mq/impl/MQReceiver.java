package dsm.mq.impl;

import dsm.base.BaseEntity;
import dsm.base.impl.UniversalEntity;
import dsm.core.ChannelInfo;
import dsm.log.LogSystem;
import dsm.log.LogSystemFactory;
import dsm.utils.SimpleUtils;
import dsm.utils.net.Receiver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * @ClassName : dsm.mq.impl.MQReceiver
 * @Description :
 * @Date 2021-05-13 14:25:28
 * @Author ZhangHL
 */
public class MQReceiver implements Runnable {

    private String name;

    private MQReceiverHandler handler;

    private List<ChannelInfo> list;

    private LogSystem log;

    public void init(String name,MQReceiverHandler handler){
        this.name = name;
        this.handler = handler;
        handler.init(new ArrayList<BaseEntity>());
        list = new ArrayList<ChannelInfo>();
        log = LogSystemFactory.getLogSystem();
    }

    @Override
    public void run() {
        Receiver receiver = new Receiver();
        receiver.init(name,handler,list);
        Thread t =new Thread(receiver);
        t.start();
        //开启心跳检测
        sync();
    }

    /**
     * 心跳检测
     */
    private void sync() {
        long interval = 200;
        try {
            while (true) {
                Iterator<ChannelInfo> iterator = list.listIterator();
                while (iterator.hasNext()) {
                    ChannelInfo info = iterator.next();
                    if (info.getBeat() > 20) {
                        info.getChannel().socket().close();
                        iterator.remove();
                        continue;
                    }
                    info.beat();
                }
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    log.error(null,e.getMessage());
                }
            }
        } catch (IOException e) {
            log.error(null,e.getMessage());
        }
    }

    public static void main(String[] args) {
        MQReceiver receiver = new MQReceiver();
        receiver.init("event.mq",new MQReceiverHandler());
        new Thread(receiver).start();
        try {
            while (true) {
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
