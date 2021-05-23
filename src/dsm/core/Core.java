package dsm.core;

import dsm.log.LogSystem;
import dsm.log.LogSystemFactory;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName : dsm.core.Core
 * @Description :
 * @Date 2021-05-17 09:15:13
 * @Author ZhangHL
 */
public class Core implements Runnable {

    private List<ChannelInfo> list;

    private LogSystem log;

    public void init() {
        list = new ArrayList<>();
        log = LogSystemFactory.getLogSystem();
    }


    @Override
    public void run() {
        startReceive();

        sync();
    }

    private void startReceive() {
        CoreReceiver receiver = new CoreReceiver();
        receiver.init(list);
        Thread t = new Thread(receiver);
        t.start();
    }

    /**
     * 心跳检测
     */
    private void sync() {
        long interval = 2000;
        while (true) {
            Iterator<ChannelInfo> iterator = list.listIterator();
            while (iterator.hasNext()) {
                ChannelInfo info = iterator.next();
                if (info.getChannel() == null) {
                    iterator.remove();
                    continue;
                }
                info.beat();
            }
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                log.error(null,e.getMessage());
            }
        }
    }

}
