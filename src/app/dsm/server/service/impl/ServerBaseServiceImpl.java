package app.dsm.server.service.impl;

import app.dsm.server.adapter.ListenerAdapter;
import app.dsm.server.annotation.Path;
import app.dsm.server.service.ServerBaseService;

import java.time.LocalDateTime;

@Path(value = "/server")
public class ServerBaseServiceImpl implements ServerBaseService {

    private ListenerAdapter listenerAdapter;

    @Override
    public void invoke(Object obj, String... args) {
        this.listenerAdapter = (ListenerAdapter) obj;
    }

    @Override
    public void setArgs(Object obj, String... args) {
        this.listenerAdapter = (ListenerAdapter) obj;
    }

    /**
     * 返回服务器时间
     * @return @return {@link String }
     * @author zhl
     * @date 2021-08-12 21:58
     * @version V1.0
     */
    @Path(value = "/gettime")
    @Override
    public String getTime(){
        return LocalDateTime.now().toString().replace("T"," ");
    }

}
