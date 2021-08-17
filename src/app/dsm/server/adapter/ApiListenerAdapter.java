package app.dsm.server.adapter;

import app.dsm.base.JSONTool;
import app.dsm.config.Configer;
import app.dsm.server.SelectorIO;
import app.dsm.server.domain.BasePath;
import app.dsm.server.http.HttpResponseBuilder;
import app.dsm.server.trigger.PathTrigger;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.parser.impl.JSONParserImpl;
import app.utils.EntityUtils;
import app.utils.SimpleUtils;
import app.utils.listener.ThreadListener;
import app.utils.net.Sender;

import java.net.CacheResponse;
import java.net.URI;

import com.sun.xml.internal.ws.api.server.InstanceResolver;
import lombok.Data;
import sun.net.www.http.HttpClient;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiListenerAdapter implements ThreadListener {

    private PathTrigger pathTrigger;

    private Object result;

    private ListenerAdapter listenerAdapter;

    private LogSystem log = LogSystemFactory.getLogSystem();

    public void initialize() {
        pathTrigger = new PathTrigger();
        pathTrigger.initialize();
        List<String> packages = new Configer().readConfigList("package.name");
        for (String str : packages){
            pathTrigger.scanPackage(str);
        }
    }

    @Override
    public void invoke(Object obj, String... args) {
        BasePath basePath = (BasePath) new JSONParserImpl().parser(listenerAdapter.getData(), BasePath.class);
        result = pathTrigger.trigger(basePath.getPath(), new String(listenerAdapter.getData()), listenerAdapter);
        if (result != null) {
            response(result);
        }
    }

    /**
     * 返回消息
     *
     * @param obj obj
     * @return
     * @author zhl
     * @date 2021-08-14 02:03
     * @version V1.0
     */
    private void response(Object obj) {
        SocketChannel socketChannel = listenerAdapter.getChannel();
        String ret = getResponse(JSONTool.toJson(obj));
        log.info("返回数据:{}到{}", ret, socketChannel);
        Sender.send(socketChannel, ret.getBytes(StandardCharsets.UTF_8));
    }


    private String getResponse(byte[] data) {
        Configer configer = new Configer();
        HttpResponseBuilder httpBuilder = new HttpResponseBuilder();
        httpBuilder.setCode("200").setServer("Server: DSMServer/1.0")
                .setHost(configer.readConfig("ip")+" "+configer.readConfig("port"));
        httpBuilder.setData(new String(data));
        return httpBuilder.toString();
    }

    @Override
    public void setArgs(Object obj, String... args) {

    }
}
