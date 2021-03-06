package app.dsm.server;

import app.utils.listener.IListener;
import app.utils.listener.ThreadListener;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public interface SelectorIO extends Runnable{


    /**
     * 初始化
     * @return
     * @author zhl
     * @date 2021-08-11 21:59
     * @version V1.0
     */
    void initialize();

    /**
     * 开启Selector
     * @param serverSocketChannel 服务器套接字通道
     * @return
     * @author zhl
     * @date 2021-08-11 20:53
     * @version V1.0
     */
    void open(ServerSocketChannel serverSocketChannel);





}
