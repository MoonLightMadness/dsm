package app.dsm.server.handler;

import app.dsm.server.adapter.Adapter;
import app.dsm.server.adapter.ApiListenerAdapter;
import app.dsm.server.adapter.ListenerAdapter;
import app.dsm.server.constant.Indicators;
import app.log.LogSystem;
import app.log.LogSystemFactory;

/**
 * APIG处理器
 * @ClassName : app.dsm.server.handler.APIGHandler
 * @Description :
 * @Date 2021-09-02 08:57:43
 * @Author ZhangHL
 */
public class APIGHandler implements Handler {

    private LogSystem log = LogSystemFactory.getLogSystem();

    @Override
    public void handle(Adapter adapter) {
        try {
            ApiListenerAdapter apiListenerAdapter = new ApiListenerAdapter();
            apiListenerAdapter.setListenerAdapter((ListenerAdapter) adapter);
            apiListenerAdapter.initialize();
            apiListenerAdapter.invoke(adapter);
            log.info("订阅方法触发完成");
        } catch (Exception e) {
            log.error("订阅方法执行失败，原因：{}", e);
            e.printStackTrace();
        }
    }
}
