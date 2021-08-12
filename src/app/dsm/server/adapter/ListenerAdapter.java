package app.dsm.server.adapter;

import app.dsm.server.impl.SelectorIOImpl;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.utils.SimpleUtils;
import app.utils.listener.ThreadListener;
import lombok.Data;

import java.nio.channels.SocketChannel;

@Data
public class ListenerAdapter implements Runnable{

    /**
     * 数据
     */
    private byte[] data;

    /**
     * 远端服务器实体
     */
    private SocketChannel channel;

    /**
     * 数据选择器
     */
    private SelectorIOImpl selectorIO;

    /**
     * 线程监听器
     */
    private ThreadListener threadListener;

    private LogSystem log = LogSystemFactory.getLogSystem();

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        data = SimpleUtils.receiveDataInNIO(channel);
        log.info("异步接收数据完成，开始触发订阅方法");
        threadListener.setArgs(this);
        threadListener.run();
        log.info("订阅方法触发完成");
    }
}
