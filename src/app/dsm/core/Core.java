package app.dsm.core;

import app.dsm.verify.IStrategy;
import app.dsm.verify.impl.Strategy;
import app.log.LogSystem;
import app.log.LogSystemFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * @ClassName : app.dsm.core.Core
 * @Description :
 * @Date 2021-05-17 09:15:13
 * @Author ZhangHL
 */
public class Core implements Runnable {

    private List<ChannelInfo> list;

    private LogSystem log;

    private String name;


    /**
     * 初始化
     * @param name 名字
     */
    public void init(String name) {
        list = new ArrayList<>();
        log = LogSystemFactory.getLogSystem();
        this.name = name;
    }


    @Override
    public void run() {

        startReceive();

        sync();
    }

    private void startReceive() {
        CoreReceiver receiver = new CoreReceiver();
        receiver.init(list,name);
        Thread t = new Thread(receiver);
        t.start();
    }

    /**
     * 心跳检测
     */
    private void sync() {
        long interval = 2000;
        try {
            while (true) {
                ListIterator<ChannelInfo> iterator = list.listIterator();
                while (iterator.hasNext()) {
                    ChannelInfo info = iterator.next();
                    if (info.getBeat() > 20) {
                        info.getChannel().socket().close();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Core core = new Core();
        core.init("core");
        Thread thread = new Thread(core);
        thread.start();

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
