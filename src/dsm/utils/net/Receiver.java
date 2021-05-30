package dsm.utils.net;

import dsm.base.BaseEntity;
import dsm.base.impl.UniversalEntity;
import dsm.core.ChannelInfo;
import dsm.log.LogSystem;
import dsm.log.LogSystemFactory;
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
import java.util.*;

/**
 * @ClassName : dsm.utils.net.Receiver
 * @Description :
 * @Date 2021-05-13 15:06:09
 * @Author ZhangHL
 */
public class Receiver implements Runnable {

    private String name;

    private ICallBack callBack;

    private List<ChannelInfo> info;

    private LogSystem log;

    public void init(String name,ICallBack callBack,List<ChannelInfo> info) {
        this.name = name;
        this.callBack=callBack;
        this.info=info;
        log = LogSystemFactory.getLogSystem();
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
                        generateChannelInfo(remote);
                        log.info(null,"接入:{}",remote.getRemoteAddress().toString());
                    }
                    if (sk.isReadable()) {
                        checkBeat((SocketChannel) sk.channel());
                        byte[] data = SimpleUtils.receiveDataInNIO((SocketChannel) sk.channel());
                        if(data.length > 0){
                            BaseEntity entity = (BaseEntity) SimpleUtils.bytesToSerializableObject(data);
                            //log.info(null,"收到:{}",entity.toString());
                            callBack.invoke((SocketChannel) sk.channel(),entity);
                        }
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            log.error(null,e.getMessage());
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
            }
            reader.close();
            if (ip == null) {
                ip = InetAddress.getLocalHost().getHostAddress();
            }
        } catch (FileNotFoundException e) {
            log.error(null,e.getMessage());
        } catch (IOException e) {
            log.error(null,e.getMessage());
        }
        address = new InetSocketAddress(ip, Integer.parseInt(port));
        return address;
    }

    private void generateChannelInfo(SocketChannel channel){
        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setChannel(channel);
        ListIterator iterator = info.listIterator();
        iterator.add(channelInfo);
    }

    private void checkBeat(SocketChannel channel){
        ListIterator<ChannelInfo> ci = info.listIterator();
        try {
            while (ci.hasNext()){
                ChannelInfo inf = ci.next();
                if(inf.getChannel().isConnected()){
                    if(inf.getChannel().getRemoteAddress().toString().equals(channel.getRemoteAddress().toString())){
                        inf.reset();
                    }
                }
            }
        } catch (IOException e) {
            log.error(null, e.getMessage());
        }
    }
}
