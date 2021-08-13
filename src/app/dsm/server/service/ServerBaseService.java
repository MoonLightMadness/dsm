package app.dsm.server.service;

import app.dsm.server.vo.CalculatorReqVO;
import app.dsm.server.vo.CalculatorRspVO;
import app.dsm.server.vo.GetTimeRspVO;

public interface ServerBaseService extends  Service{
    /**
     * 返回服务器时间
     * @return @return {@link String }
     * @author zhl
     * @date 2021-08-12 21:58
     * @version V1.0
     */
    public GetTimeRspVO getTime();

    /**
     * 计算两数的和
     * @param calculatorReqVO
     * @return @return {@link CalculatorRspVO }
     * @author zhl
     * @date 2021-08-13 23:26
     * @version V1.0
     */
    public CalculatorRspVO calculate(CalculatorReqVO calculatorReqVO);
}
