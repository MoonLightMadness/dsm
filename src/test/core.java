package test;

import app.dsm.base.JSONTool;
import app.dsm.base.impl.UniversalEntity;
import app.dsm.base.impl.UniversalEntityWrapper;
import app.dsm.core.Core;
import app.dsm.service.impl.Service;
import app.dsm.service.impl.ServiceMessageHandler;
import app.utils.SimpleUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class core {

    @Test
    public void test1() throws IOException {
        Core core = new Core();
        core.init("core");
        Thread thread = new Thread(core);
        thread.start();
        try {
            while (true){

                SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(InetAddress.getLocalHost(),9002));
                Thread.sleep(400);
                UniversalEntity entity = UniversalEntityWrapper.getOne(String.valueOf(System.currentTimeMillis()),
                        "1",
                        "test",
                        "core",
                        "1",
                        "set_name "+socketChannel.getLocalAddress().toString()+" "+ System.currentTimeMillis(),
                        "null",
                        "00001");
                byte[] data = SimpleUtils.serializableToBytes(entity);
                ByteBuffer buffer = ByteBuffer.allocate(data.length);
                buffer.put(data);
                buffer.flip();
                socketChannel.write(buffer);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() throws IOException {
        Core core = new Core();
        core.init("core");
        Thread thread = new Thread(core);
        thread.start();
        try {
            //先注册一个服务
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(InetAddress.getLocalHost(),9002));
            Thread.sleep(400);
            UniversalEntity entity = UniversalEntityWrapper.getOne(String.valueOf(System.currentTimeMillis()),
                    "1",
                    "test",
                    "core",
                    "1",
                    "set_name test "+socketChannel.getLocalAddress().toString(),
                    "null",
                    "00001");
            byte[] data = JSONTool.toJson(entity);
            ByteBuffer buffer = ByteBuffer.allocate(data.length);
            buffer.put(data);
            buffer.flip();
            socketChannel.write(buffer);
            Thread.sleep(1000);
            //获取这个服务的IP
            SocketChannel socketChannel1 = SocketChannel.open(new InetSocketAddress(InetAddress.getLocalHost(),9002));
            Thread.sleep(400);
            UniversalEntity entity1 = UniversalEntityWrapper.getOne(String.valueOf(System.currentTimeMillis()),
                    "1",
                    "test",
                    "core",
                    "1",
                    "get_ip test",
                    "null",
                    "00001");
            byte[] data1 = JSONTool.toJson(entity1);
            ByteBuffer buffer1 = ByteBuffer.allocate(data1.length);
            buffer1.put(data1);
            buffer1.flip();
            socketChannel1.write(buffer1);
            Thread.sleep(100);
//            buffer.clear();
//            socketChannel1.read(buffer);
//            buffer.flip();
//            System.out.println(new String(buffer.array()));
            socketChannel1.configureBlocking(false);
            byte[] res = SimpleUtils.receiveDataInNIO(socketChannel1);
            UniversalEntity entity2 = (UniversalEntity) JSONTool.getObject(buffer.array(),UniversalEntity.class);

            System.out.println(entity2.toString());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3(){
//        Core core = new Core();
//        core.init("core");
//        Thread thread = new Thread(core);
//        thread.start();
        Service receiver = new Service();
        receiver.init("event.mq","core",new ServiceMessageHandler());
        new Thread(receiver).start();
        try {
            Thread.sleep(1000);
            SocketChannel socketChannel1 = SocketChannel.open(new InetSocketAddress(InetAddress.getLocalHost(),9002));
            Thread.sleep(400);
            UniversalEntity entity1 = UniversalEntityWrapper.getOne(String.valueOf(System.currentTimeMillis()),
                    "1",
                    "test",
                    "core",
                    "1",
                    "get_ip mq",
                    "null",
                    "00001");
            byte[] data1 = JSONTool.toJson(entity1);
            ByteBuffer buffer1 = ByteBuffer.allocate(data1.length);
            buffer1.put(data1);
            buffer1.flip();
            socketChannel1.write(buffer1);
            Thread.sleep(1000);
//            ByteBuffer buffer = ByteBuffer.allocate(1024);
//            socketChannel1.read(buffer);
            UniversalEntity entity3 = (UniversalEntity) JSONTool.getObject(SimpleUtils.receiveDataInNIO(socketChannel1),UniversalEntity.class);
            System.out.println(entity3.toString());
            while (true) {
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test4(){
        try {
            SocketChannel socketChannel1 = SocketChannel.open(new InetSocketAddress(InetAddress.getLocalHost(),9002));
            Thread.sleep(400);
            UniversalEntity entity1 = UniversalEntityWrapper.getOne(String.valueOf(System.currentTimeMillis()),
                    "1",
                    "test",
                    "core",
                    "1",
                    "get_ip mq",
                    "null",
                    "00001");
            byte[] data1 = JSONTool.toJson(entity1);
            ByteBuffer buffer1 = ByteBuffer.allocate(data1.length);
            buffer1.put(data1);
            buffer1.flip();
            socketChannel1.write(buffer1);
            Thread.sleep(1000);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            socketChannel1.read(buffer);
            UniversalEntity entity3 = (UniversalEntity) JSONTool.getObject(buffer.array(),UniversalEntity.class);
            System.out.println(entity3.toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
