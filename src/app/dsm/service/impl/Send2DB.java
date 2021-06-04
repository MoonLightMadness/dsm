package app.dsm.service.impl;

import app.dsm.base.impl.UniversalEntity;
import app.utils.SimpleUtils;

import java.nio.channels.SocketChannel;

public class Send2DB {

    public static void send(SocketChannel socketChannel, UniversalEntity entity){
        byte[] data = SimpleUtils.serializableToBytes(entity);
        ServiceSender.send(socketChannel,data);
    }
}
