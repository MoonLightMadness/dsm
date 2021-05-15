package dsm.mq.impl;

import dsm.base.BaseEntity;
import dsm.base.impl.UniversalEntity;
import dsm.utils.SimpleUtils;

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
import java.util.Iterator;
import java.util.Locale;
import java.util.Queue;

/**
 * @ClassName : dsm.mq.impl.MQReceiver
 * @Description :
 * @Date 2021-05-13 14:25:28
 * @Author ZhangHL
 */
public class MQReceiver implements Runnable {

    private Queue<BaseEntity> msg;

    public void init(Queue queue) {
        this.msg = queue;
    }

    @Override
    public void run() {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel channel=ServerSocketChannel.open();
            channel.bind(readConfig());
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_ACCEPT);
            while (true){
                int num = selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey sk = iterator.next();
                    if(sk.isAcceptable()){
                        SocketChannel remote = ((ServerSocketChannel)sk.channel()).accept();
                        remote.configureBlocking(false);
                        remote.register(selector,SelectionKey.OP_READ);
                    }
                    if(sk.isReadable()){
                        byte[] data = SimpleUtils.receiveDataInNIO((SocketChannel) sk.channel());
                        BaseEntity entity = (BaseEntity) SimpleUtils.bytesToSerializableObject(data);
                        if("query".equals(((UniversalEntity) entity).getMessageType())){
                            msg.add(entity);
                        }
                        if("db_get".equals(((UniversalEntity) entity).getMessageType())){
                            Send2DB.send(((SocketChannel) sk.channel()), (UniversalEntity) entity);
                        }
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SocketAddress readConfig() {
        String file = "./config.txt";
        String ip = null, port = null;
        SocketAddress address = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String temp;
            while ((temp = reader.readLine()) != null) {
                temp = temp.toLowerCase(Locale.ROOT);
                temp = temp.replaceAll(" ", "");
                if (temp.startsWith("mq.ip=")) {
                    ip = temp.substring("mq.ip=".length());
                }
                if (temp.startsWith("mq.port")) {
                    port = temp.substring("mq.port=".length());
                }
            }
            if (ip == null) {
                ip = InetAddress.getLocalHost().getHostAddress();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        address = new InetSocketAddress(ip, Integer.parseInt(port));
        return address;
    }

}
