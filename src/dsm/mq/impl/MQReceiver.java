package dsm.mq.impl;

import dsm.base.BaseEntity;
import dsm.base.impl.UniversalEntity;
import dsm.core.ChannelInfo;
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

    //private List<BaseEntity> msg;

//    public void init(List queue) {
//        this.msg = queue;
//    }

    private String name;

    private MQReceiverHandler handler;

    public void init(String name,MQReceiverHandler handler){
        this.name = name;
        this.handler = handler;
        handler.init(new ArrayList<BaseEntity>());
    }

    @Override
    public void run() {
        Receiver receiver = new Receiver();
        receiver.init(name,handler,new ArrayList<ChannelInfo>());
        Thread t =new Thread(receiver);
        t.start();
    }


}
