package dsm.mq.impl;

import dsm.base.impl.UniversalEntity;
import dsm.utils.SimpleUtils;

import java.nio.channels.SocketChannel;

public class Send2DB {

    public static void send(SocketChannel socketChannel, UniversalEntity entity){
        byte[] data = SimpleUtils.serializableToBytes(entity);
        MQSender.send(socketChannel,data);
    }
}
