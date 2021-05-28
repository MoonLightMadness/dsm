package test;

import dsm.base.impl.UniversalEntity;
import dsm.base.impl.UniversalEntityWrapper;
import dsm.mq.impl.MQReceiver;
import dsm.mq.impl.MQReceiverHandler;
import dsm.utils.SimpleUtils;
import dsm.utils.net.Receiver;
import dsm.utils.net.Sender;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @ClassName : test.mq
 * @Description :
 * @Date 2021-05-28 14:51:03
 * @Author ZhangHL
 */
public class mq {
    @Test
    public void test1(){
        try {
            //开启消息队列服务
//            MQReceiver receiver = new MQReceiver();
//            receiver.init("event.mq",new MQReceiverHandler());
//            new Thread(receiver).start();
//            Thread.sleep(100);
            //注册一个消费者
            SocketChannel socketChannel1 = SocketChannel.open(new InetSocketAddress(InetAddress.getLocalHost(),9003));
            UniversalEntity entity1 = UniversalEntityWrapper.getOne(String.valueOf(System.currentTimeMillis()),
                    "1",
                    "test",
                    "core",
                    "1",
                    "register event1^event2",
                    "null",
                    "00001");
            Sender.send(socketChannel1, SimpleUtils.serializableToBytes(entity1));
            Thread.sleep(100);
            //向消息队列发送信息
            SocketChannel socketChannel2 = SocketChannel.open(new InetSocketAddress(InetAddress.getLocalHost(),9003));
            UniversalEntity entity2 = UniversalEntityWrapper.getOne(String.valueOf(System.currentTimeMillis()),
                    "1",
                    "test",
                    "core",
                    "1",
                    "message",
                    "event1",
                    "00001");
            Sender.send(socketChannel2, SimpleUtils.serializableToBytes(entity2));
            Thread.sleep(1000);
            socketChannel1.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            socketChannel1.read(buffer);
            UniversalEntity entity3 = (UniversalEntity) SimpleUtils.bytesToSerializableObject(buffer.array());
            System.out.println(entity3.toString());
            Thread.sleep(4000);
            Sender.send(socketChannel1, SimpleUtils.serializableToBytes(entity1));
            Sender.send(socketChannel2, SimpleUtils.serializableToBytes(entity2));
            Thread.sleep(500);
            buffer.clear();
            socketChannel1.read(buffer);
            entity3 = (UniversalEntity) SimpleUtils.bytesToSerializableObject(buffer.array());
            System.out.println(entity3.toString());
            Thread.sleep(5000);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
