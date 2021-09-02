package app.dsm.server.handler;

import app.dsm.server.adapter.Adapter;
import app.log.LogSystem;
import app.log.LogSystemFactory;

/**
 * 控制台处理器
 * @ClassName : app.dsm.server.handler.ConsoleHandler
 * @Description :
 * @Date 2021-09-02 08:58:06
 * @Author ZhangHL
 */
public class ConsoleHandler implements Handler {

    private LogSystem log = LogSystemFactory.getLogSystem();


    private Adapter adapter;

    @Override
    public void handle(Adapter adapter) {

    }
}
