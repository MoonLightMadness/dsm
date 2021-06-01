package app.dsm.core;

import app.dsm.log.LogSystem;
import app.dsm.log.LogSystemFactory;
import app.dsm.utils.net.Receiver;

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

    public void init(List<ChannelInfo> list){
        log = LogSystemFactory.getLogSystem();
        this.list=list;
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
