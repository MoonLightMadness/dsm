package dsm.utils.net;

import dsm.base.BaseEntity;
import dsm.base.impl.UniversalEntity;
import dsm.utils.SimpleUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Locale;
import java.util.Queue;

/**
 * @ClassName : dsm.utils.net.Receiver
 * @Description :
 * @Date 2021-05-13 15:06:09
 * @Author ZhangHL
 */
public class Receiver implements Runnable {
    private Queue<BaseEntity> msg;

    private String name;

    private String strategy;

    public void init(String name, Queue queue) {
        this.name = name;
        this.msg = queue;
    }

    @Override
    public void run() {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel channel = ServerSocketChannel.open();
            channel.bind(readConfig());
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                int num = selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey sk = iterator.next();
                    if (sk.isAcceptable()) {
                        SocketChannel remote = ((ServerSocketChannel) sk.channel()).accept();
                        remote.configureBlocking(false);
                        remote.register(selector, SelectionKey.OP_READ);
                    }
                    if (sk.isReadable()) {
                        byte[] data = SimpleUtils.receiveDataInNIO((SocketChannel) sk.channel());
                        BaseEntity entity = (BaseEntity) SimpleUtils.bytesToSerializableObject(data);
                        Class temp = (Class) Class.forName(strategy).newInstance();
                        Method m = temp.getDeclaredMethod("invoke", Array.class);
                        m.setAccessible(true);
                        m.invoke(temp,data);
                    }
                    iterator.remove();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
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
                if (temp.startsWith(name+".ip=")) {
                    ip = temp.substring((name+".ip=").length());
                }
                if (temp.startsWith(name+".port")) {
                    port = temp.substring((name+".port=").length());
                }
                if(temp.startsWith(name+".strategy=")){
                    strategy = temp.substring((name+".strategy=").length());
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
