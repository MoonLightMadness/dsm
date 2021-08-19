package app.dsm.server.vo;

import lombok.Data;

/**
 * @ClassName : app.dsm.server.vo.NoPowerBaseRspVO
 * @Description :
 * @Date 2021-08-18 09:16:15
 * @Author ZhangHL
 */
@Data
public class NoPowerBaseRspVO {

    private String code = "999999";

    private String msg = "权限不足";

}
