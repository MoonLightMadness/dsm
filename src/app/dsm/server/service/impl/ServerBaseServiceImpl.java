package app.dsm.server.service.impl;

import app.dsm.server.SelectorIO;
import app.dsm.server.adapter.ListenerAdapter;
import app.dsm.server.annotation.Authority;
import app.dsm.server.annotation.Path;
import app.dsm.server.container.ServerContainer;
import app.dsm.server.container.ServerEntity;
import app.dsm.server.domain.BasePath;
import app.dsm.server.impl.SelectorIOImpl;
import app.dsm.server.vo.*;
import app.dsm.server.service.ServerBaseService;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.parser.impl.JSONParserImpl;
import app.utils.SimpleUtils;
import app.utils.TimeFormatter;

import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ListIterator;

@Path(value = "/server")
public class ServerBaseServiceImpl implements ServerBaseService {

    private ListenerAdapter listenerAdapter;

    private LogSystem log = LogSystemFactory.getLogSystem();

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
    public GetTimeRspVO getTime(String args){
        GetTimeRspVO getTimeRspVO = new GetTimeRspVO();
        getTimeRspVO.setTime(SimpleUtils.getTimeStamp2(TimeFormatter.SEC_LEVEL));
        return getTimeRspVO;
    }

    @Path(value = "/calculate")
    @Override
    public CalculatorRspVO calculate(String args) {
        CalculatorReqVO calculatorReqVO = (CalculatorReqVO) new JSONParserImpl().parser(args.getBytes(StandardCharsets.UTF_8),CalculatorReqVO.class);
        CalculatorRspVO calculatorRspVO = new CalculatorRspVO();
        try {
            long result = Long.parseLong(calculatorReqVO.getX()) + Long.parseLong(calculatorReqVO.getY());
            calculatorRspVO.setResult(String.valueOf(result));
            return calculatorRspVO;
        }catch (Exception e) {
            log.error("计算失败,原因:{}",e);
            CalculatorRspVO baseRspVO = new CalculatorRspVO();
            baseRspVO.setCode("999999");
            baseRspVO.setMsg(e.getMessage());
            return  baseRspVO;
        }

    }

    /**
     * 设置服务器名字
     *
     * @param args
     * @return @return {@link BaseRspVO }
     * @author zhl
     * @date 2021-08-14 11:37
     * @version V1.0
     */
    @Path(value = "/setname")
    @Override
    public BaseRspVO setName(String args) {
        SetNameReqVO setNameReqVO = (SetNameReqVO) new JSONParserImpl().parser(args.getBytes(StandardCharsets.UTF_8),SetNameReqVO.class);
        SocketChannel channel = listenerAdapter.getChannel();
        SelectorIOImpl selectorIO = listenerAdapter.getSelectorIO();
        ServerContainer serverContainer = selectorIO.getServerContainer();
        List<ServerEntity> entityList = serverContainer.getList();
        ListIterator<ServerEntity> iterator = entityList.listIterator();
        while (iterator.hasNext()){
            ServerEntity entity = iterator.next();
            if(entity.getSocketChannel() == channel){
                entity.setName(setNameReqVO.getName());
            }
        }
        BaseRspVO baseReqVO = new BaseRspVO();
        baseReqVO.setCode("200");
        baseReqVO.setMsg("success");
        return baseReqVO;
    }
}
