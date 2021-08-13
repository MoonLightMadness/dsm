package app.dsm.server.service.impl;

import app.dsm.server.adapter.ListenerAdapter;
import app.dsm.server.annotation.Path;
import app.dsm.server.domain.BasePath;
import app.dsm.server.vo.CalculatorReqVO;
import app.dsm.server.vo.CalculatorRspVO;
import app.dsm.server.vo.GetTimeRspVO;
import app.dsm.server.service.ServerBaseService;
import app.parser.impl.JSONParserImpl;
import app.utils.SimpleUtils;
import app.utils.TimeFormatter;

import java.nio.charset.StandardCharsets;

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
        long result = Long.parseLong(calculatorReqVO.getX()) + Long.parseLong(calculatorReqVO.getY());
        calculatorRspVO.setResult(String.valueOf(result));
        return calculatorRspVO;
    }
}
