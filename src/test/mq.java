package test;

import app.dsm.base.JSONTool;
import app.dsm.base.impl.UniversalEntity;
import app.dsm.base.impl.UniversalEntityWrapper;
import app.dsm.service.impl.Service;
import app.dsm.service.impl.ServiceMessageHandler;
import app.utils.SimpleUtils;
import app.utils.net.Sender;
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
            Service receiver = new Service();
            receiver.init("event.mq","core",new ServiceMessageHandler());
            new Thread(receiver).start();
            Thread.sleep(100);
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
            Sender.send(socketChannel1, JSONTool.toJson(entity1));
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
            Sender.send(socketChannel2, JSONTool.toJson(entity2));
            Thread.sleep(1000);
            socketChannel1.configureBlocking(false);
            UniversalEntity entity3 = (UniversalEntity) JSONTool.getObject(SimpleUtils.receiveDataInNIO(socketChannel1),UniversalEntity.class);
            System.out.println(entity3.toString());
            Thread.sleep(4000);
            Sender.send(socketChannel1, JSONTool.toJson(entity1));
            Sender.send(socketChannel2, JSONTool.toJson(entity2));
            Thread.sleep(500);
            entity3 = (UniversalEntity) JSONTool.getObject(SimpleUtils.receiveDataInNIO(socketChannel1),UniversalEntity.class);
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
