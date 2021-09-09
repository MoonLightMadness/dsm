package app.utils;

import app.log.LogSystem;
import app.log.LogSystemFactory;

import java.util.UUID;
import java.util.concurrent.*;

/**
 * @ClassName : app.utils.ThreadUitls
 * @Description :
 * @Date 2021-09-09 13:51:55
 * @Author ZhangHL
 */
public class ThreadUitls {

    private static ThreadFactory namedThreadFactory;

    private static ExecutorService singleThreadPool;

    private static LogSystem log = LogSystemFactory.getLogSystem();

    static {
        //设置命名规则
        namedThreadFactory = r -> {
            Thread thread = new Thread(r);
            String id = UUID.randomUUID().toString();
            thread.setName(r.getClass().getSimpleName() + id);
            log.info("{}加入线程池", r.getClass().getSimpleName() + id);
            return thread;
        };
        //初始化线程池
        singleThreadPool = new ThreadPoolExecutor(16, 128,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    public static Future submit(Runnable runnable) {
        return singleThreadPool.submit(runnable);
    }

}
