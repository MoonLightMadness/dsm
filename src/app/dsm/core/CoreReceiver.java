package app.dsm.core;

import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.utils.net.Receiver;

import java.nio.channels.ServerSocketChannel;
import java.util.List;

/**
 * @ClassName : app.dsm.core.CoreReceiver
 * @Description :
 * @Date 2021-05-17 09:25:24
 * @Author ZhangHL
 */
public class CoreReceiver implements Runnable{

    private List<ChannelInfo> list;

    private ServerSocketChannel server;

    private CoreMessageHandler handler;

    private LogSystem log;

    private String name;

    public void init(List<ChannelInfo> list,String name){
        log = LogSystemFactory.getLogSystem();
        this.list=list;
        handler = new CoreMessageHandler();
        handler.init(list);
        this.name = name;
    }

    @Override
    public void run() {
        Receiver receiver = new Receiver();
        receiver.init(name,handler,list);
        Thread thread = new Thread(receiver);
        thread.start();
    }


}
