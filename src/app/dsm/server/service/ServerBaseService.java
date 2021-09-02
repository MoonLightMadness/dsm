package app.dsm.server.service;

import app.dsm.server.domain.BasePath;
import app.dsm.server.vo.BaseRspVO;
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
    public GetTimeRspVO getTime(GetTimeRspVO args);

    /**
     * 计算两数的和
     * @param calculatorReqVO
     * @return @return {@link CalculatorRspVO }
     * @author zhl
     * @date 2021-08-13 23:26
     * @version V1.0
     */
    public CalculatorRspVO calculate(CalculatorReqVO args);

    /**
     * 设置服务器名字
     * @param args
     * @return @return {@link BaseRspVO }
     * @author zhl
     * @date 2021-08-14 11:37
     * @version V1.0
     */
    public BaseRspVO setName(String args);
}
