package app.dsm.server.impl;

import app.dsm.config.Configer;
import app.dsm.server.BeatChecker;
import app.dsm.server.DataIO;
import app.dsm.server.SelectorIO;
import app.dsm.server.Server;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.utils.listener.IListener;
import app.utils.listener.ThreadListener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

public class ServerImpl implements Server {

    /**
     * 服务器套接字通道
     */
    private ServerSocketChannel serverSocketChannel;

    /**
     * 数据输入输出类
     */
    private DataIO dataIO;

    private LogSystem log;

    private Configer configer;

    private SelectorIO selectorIO;



    @Override
    public void initialize(){
        log = LogSystemFactory.getLogSystem();
        configer = new Configer();
        selectorIO = new SelectorIOImpl();
        selectorIO.initialize();
    }

    @Override
    public void open() {
        openServer();
    }

    private void openServer(){
        try {
            log.info("开启服务器");
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(configer.readConfig("ip"), Integer.parseInt(configer.readConfig("port"))));
            serverSocketChannel.configureBlocking(false);
            selectorIO.open(serverSocketChannel);
            //开启线程
            new Thread(selectorIO).start();
        } catch (IOException e) {
            log.error("服务器运行失败，原因:{}",e);
            e.printStackTrace();
        }
    }


}
