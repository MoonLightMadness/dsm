package app.dsm.server.adapter;

import app.dsm.base.JSONTool;
import app.dsm.config.Configer;
import app.dsm.server.SelectorIO;
import app.dsm.server.domain.BasePath;
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

//    POST / HTTP/1.1
//    Content-Type: application/json
//    User-Agent: PostmanRuntime/7.28.1
//    Accept: */*
//Postman-Token: 79577fc3-5568-4d1b-9afd-e4d7cb7a46e2
//Host: 127.0.0.1:9004
//Accept-Encoding: gzip, deflate, br
//Connection: keep-alive
//Content-Length: 36
//
//{
//
//    "path":"/server/gettime"
//}

    private String getResponse(byte[] data) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK").append("\n");
        sb.append("Server: DSMServer/1.0").append("\n");
        sb.append("Content-Type: Application/json").append("\n");
        sb.append("Date: ").append(LocalDateTime.now().toString()).append("\n");
        sb.append("Connection: Close").append("\n");
        Configer configer = new Configer();
        sb.append("Server:").append(configer.readConfig("ip")).append(":").append(configer.readConfig("port")).append("\n");
        sb.append("\r\n");
        String sdata = new String(data);
        sdata = sdata.substring(1,sdata.length()-1);
        sb.append(sdata.replace("\\","")).append("\n");
        return sb.toString();
    }

    @Override
    public void setArgs(Object obj, String... args) {

    }
}
