package dsm.mq.impl;

import dsm.base.BaseEntity;
import dsm.base.impl.UniversalEntity;
import dsm.base.impl.UniversalEntityWrapper;
import dsm.config.impl.UniversalConfigReader;
import dsm.core.ChannelInfo;
import dsm.log.LogSystem;
import dsm.log.LogSystemFactory;
import dsm.utils.SimpleUtils;
import dsm.utils.net.Receiver;
import dsm.utils.net.Sender;

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

    private String[] aboutCore;

    private SocketChannel coreChannel;

    public void init(String name,MQReceiverHandler handler){
        this.name = name;
        this.handler = handler;
        handler.init(new ArrayList<BaseEntity>());
        list = new ArrayList<ChannelInfo>();
        log = LogSystemFactory.getLogSystem();
        //读取core地址
        UniversalConfigReader reader =new  UniversalConfigReader();
        reader.setName("core");
        aboutCore = new String[2];
        aboutCore = reader.read();
        try {
            coreChannel = coreChannel.open(new InetSocketAddress(aboutCore[0], Integer.parseInt(aboutCore[1])));
            Thread.sleep(100);
            Sender.send(coreChannel,SimpleUtils.serializableToBytes(constructSetNameEntity()));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
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
     * 心跳检测,在每个周期向core发送心跳包
     */
    private void sync() {
        long interval = 2000;
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
                //向core发送

                Sender.send(coreChannel,SimpleUtils.serializableToBytes(constructBeatEntity()));
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

    private BaseEntity constructBeatEntity(){
        UniversalConfigReader reader =new  UniversalConfigReader();
        reader.setName("event.mq");
        String[] res = reader.read();
        StringBuilder sb = new StringBuilder();
        sb.append("/").append(res[0]).append(":").append(res[1]);
        UniversalEntity entity = UniversalEntityWrapper.getOne("0",
                "0",
                "mq",
                "core",
                "1",
                sb.toString(),
                "beat",
                "1");
        return entity;
    }

    private BaseEntity constructSetNameEntity(){
        UniversalConfigReader reader =new  UniversalConfigReader();
        reader.setName("event.mq");
        String[] res = reader.read();
        StringBuilder sb = new StringBuilder();
        sb.append("/").append(res[0]).append(":").append(res[1]);
        UniversalEntity entity = UniversalEntityWrapper.getOne("0",
                "0",
                "mq",
                "core",
                "1",
                "set_name mq "+sb.toString(),
                "set",
                "1");
        return entity;
    }

}
