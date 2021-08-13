package app.dsm.server.service.impl;

import app.dsm.server.adapter.ListenerAdapter;
import app.dsm.server.annotation.Path;
import app.dsm.server.vo.GetTimeRspVO;
import app.dsm.server.service.ServerBaseService;
import app.utils.SimpleUtils;
import app.utils.TimeFormatter;

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
    public GetTimeRspVO getTime(){
        GetTimeRspVO getTimeRspVO = new GetTimeRspVO();
        getTimeRspVO.setTime(SimpleUtils.getTimeStamp2(TimeFormatter.SEC_LEVEL));
        return getTimeRspVO;
    }

}
