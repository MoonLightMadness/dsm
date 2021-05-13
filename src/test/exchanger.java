package test;

import dsm.base.impl.UniversalEntity;
import dsm.base.impl.UniversalEntityWrapper;
import dsm.config.impl.IPConfigReader;
import dsm.exchange.FileReceiver;
import dsm.exchange.FileSender;
import dsm.exchange.impl.FileReceiverImpl;
import dsm.exchange.impl.FileSenderImpl;
import dsm.exchange.impl.GateExchangerCore;
import dsm.security.SecurityService;
import dsm.security.excp.DecodeException;
import dsm.security.excp.EncodeException;
import dsm.security.higher.UniversalHigherEncoder;
import dsm.security.impl.*;
import dsm.utils.SimpleUtils;
import dsm.utils.guid.impl.SnowFlake;
import dsm.utils.special.ExchangerUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName : test.exchanger
 * @Description :
 * @Date 2021-05-02 18:39:21
 * @Author ZhangHL
 */
public class exchanger {
    @Test
    public void test(){
        IPConfigReader reader=new IPConfigReader();
        String[] config=reader.read();
        try {


            GateExchangerCore gateExchangerCore=new GateExchangerCore();
            gateExchangerCore.init();
            Thread thread=new Thread(gateExchangerCore);
            thread.start();

            SocketChannel socketChannel=SocketChannel.open(new InetSocketAddress(config[0],Integer.parseInt(config[1])));
            Thread.sleep(100);
            UniversalEntity entity= UniversalEntityWrapper.getOne("1",
                    "23",
                    "test",
                    "to:self",
                    "23",
                    "This is a test",
                    "Test",
                    "123");

            EntitySecurityServiceImpl securityService=new EntitySecurityServiceImpl();
            securityService.init(new AESEncoder(),new AESDecoder());
            entity= (UniversalEntity) securityService.encode(entity,entity.getCompressCode());
            byte[] bytes=SimpleUtils.serializableToBytes(entity);
            ByteBuffer buffer=ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            socketChannel.write(buffer);
            Thread.sleep(1000);
        } catch (IOException | InterruptedException | EncodeException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void serviceTest(){
        IPConfigReader reader=new IPConfigReader();
        String[] config=reader.read();
        try {
            GateExchangerCore gateExchangerCore = new GateExchangerCore();
            gateExchangerCore.init();
            Thread thread = new Thread(gateExchangerCore);
            thread.start();

            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(config[0], Integer.parseInt(config[1])));
            socketChannel.configureBlocking(false);
            Thread.sleep(100);

            UniversalEntity entity = UniversalEntityWrapper.getOne("1",
                    "service",
                    "test",
                    "to:self",
                    "23",
                    "This is a test",
                    "Test",
                    "123");
            EntitySecurityServiceImpl securityService = new EntitySecurityServiceImpl();
            securityService.init(new AESEncoder(), new AESDecoder());
            ExchangerUtils.Send(socketChannel,entity.getCompressCode(),entity,securityService);
            Thread.sleep(100);
            entity= (UniversalEntity) ExchangerUtils.Receive(socketChannel,entity.getCompressCode(),entity,securityService);
            System.out.println(entity.toString());
            //获取服务列表
            entity = UniversalEntityWrapper.getOne("1",
                    "get_list",
                    "test",
                    "to:self",
                    "23",
                    "This is a test",
                    "Test",
                    "123");
            ExchangerUtils.Send(socketChannel,entity.getCompressCode(),entity,securityService);
            Thread.sleep(100);
            entity= (UniversalEntity) ExchangerUtils.Receive(socketChannel,entity.getCompressCode(),entity,securityService);
            System.out.println(entity.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private byte[] mergeBytesList(int count,List<byte[]> recv) {
        byte[] res = new byte[count];
        int pointer = 0;
        for (byte[] b : recv) {
            if (pointer >= count) {
                break;
            }
            System.arraycopy(b, 0, res, pointer, b.length);
            pointer += b.length;
        }
        return res;
    }

    @Test
    public void connectSumTest(){
        IPConfigReader reader=new IPConfigReader();
        String[] config=reader.read();
        try {


            GateExchangerCore gateExchangerCore=new GateExchangerCore();
            gateExchangerCore.init();
            Thread thread=new Thread(gateExchangerCore);
            thread.start();
            List<SocketChannel> list=new ArrayList<>();
            SnowFlake sf=new SnowFlake();

            for (int i=0;i<10000;i++){
                SocketChannel socketChannel=SocketChannel.open(new InetSocketAddress(config[0],Integer.parseInt(config[1])));
                list.add(socketChannel);
                UniversalEntity entity=new UniversalEntity();
                Thread.sleep(100);
                entity.setTimestamp(SimpleUtils.getTimeStamp());
                entity.setMessage("Hello gate,this is a test--{"+i+"}");
                entity.setHashCode(new Sha1Encoder().encode(entity.str4Hash(),null));
                entity.setGuid(sf.generateGuid("01"));
                byte[] bytes=SimpleUtils.serializableToBytes(entity);
                SecurityService securityService=new SecurityServiceImpl();
                securityService.init(new AESEncoder(),null);
                ((SecurityServiceImpl)securityService).setAccessKey("123");
                bytes=securityService.encode(new String(bytes)).getBytes(StandardCharsets.UTF_8);
                ByteBuffer buffer=ByteBuffer.allocate(bytes.length);
                buffer.put(bytes);
                buffer.flip();
                socketChannel.write(buffer);
                buffer.clear();

            }
        } catch (IOException | EncodeException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void fileChangerTest() throws IOException {
        String path = "C:\\Users\\Administrator\\Pictures\\1.jpg";
        FileSender sender = new FileSenderImpl();
        FileReceiver receiver = new FileReceiverImpl();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(
                new InetSocketAddress(InetAddress.getLocalHost(),8999));
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Selector selector = Selector.open();
                    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                    while (true){
                        int num = selector.select();
                        Iterator iterator = selector.selectedKeys().iterator();
                        while (iterator.hasNext()){
                            SelectionKey sk = (SelectionKey) iterator.next();
                            if(sk.isAcceptable()){
                                SocketChannel channel = serverSocketChannel.accept();
                                channel.configureBlocking(false);
                                channel.register(selector,SelectionKey.OP_READ);
                            }
                            if(sk.isReadable()){
                                receiver.receive((SocketChannel) sk.channel(),"./recv.jpg");
                            }
                            iterator.remove();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        SocketChannel channel = SocketChannel.open(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(),8999));
        channel.configureBlocking(false);
        sender.sendFile(path,channel);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
