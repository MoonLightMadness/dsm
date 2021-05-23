package dsm.core;

import dsm.base.impl.UniversalEntity;
import dsm.config.impl.IPConfigReader;
import dsm.log.LogSystem;
import dsm.log.LogSystemFactory;
import dsm.utils.SimpleUtils;
import dsm.utils.net.Receiver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @ClassName : dsm.core.CoreReceiver
 * @Description :
 * @Date 2021-05-17 09:25:24
 * @Author ZhangHL
 */
public class CoreReceiver implements Runnable{

    private List<ChannelInfo> list;

    private ServerSocketChannel server;

    private CoreMessageHandler handler;

    private LogSystem log;

    public void init(List<ChannelInfo> list){
        log = LogSystemFactory.getLogSystem();
        this.list=list;
//        try {
//            server = ServerSocketChannel.open();
//            String port = IPConfigReader.readPortByName("core");
//            server.bind(new InetSocketAddress(InetAddress.getLocalHost(),Integer.parseInt(port)));
//            server.configureBlocking(false);
//        } catch (IOException e) {
//            log.error(null,e.getMessage());
//        }
        handler = new CoreMessageHandler();
        handler.init(list);
    }

    @Override
    public void run() {
        Receiver receiver = new Receiver();
        receiver.init("core",handler,list);
        Thread thread = new Thread(receiver);
        thread.start();
    }


}
