package app.dsm.server.adapter;

import app.dsm.base.JSONTool;
import app.dsm.config.Configer;
import app.dsm.server.SelectorIO;
import app.dsm.server.domain.BasePath;
import app.dsm.server.trigger.PathTrigger;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.parser.impl.JSONParserImpl;
import app.utils.SimpleUtils;
import app.utils.listener.ThreadListener;
import app.utils.net.Sender;
import lombok.Data;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.channels.SocketChannel;

@Data
public class ApiListenerAdapter implements ThreadListener {

    private PathTrigger pathTrigger;

    private Object result;

    private ListenerAdapter listenerAdapter;

    private LogSystem log = LogSystemFactory.getLogSystem();

    public void initialize(){
        pathTrigger = new PathTrigger();
        pathTrigger.initialize();
        pathTrigger.scanPackage(new Configer().readConfig("package.name"));
    }

    @Override
    public void invoke(Object obj, String... args) {
        BasePath basePath = (BasePath) new JSONParserImpl().parser(listenerAdapter.getData(), BasePath.class);
        result = pathTrigger.trigger(basePath.getPath(), new String(listenerAdapter.getData()));
        if(result != null){
            response(result);
        }
    }

    /**
     * 返回消息
     * @param obj obj
     * @return
     * @author zhl
     * @date 2021-08-14 02:03
     * @version V1.0
     */
    private void response(Object obj){
        SocketChannel socketChannel = listenerAdapter.getChannel();
        log.info("返回数据:{}到{}",obj,socketChannel);
        Sender.send(socketChannel, JSONTool.toJson(obj));
    }

    @Override
    public void setArgs(Object obj, String... args) {

    }
}
