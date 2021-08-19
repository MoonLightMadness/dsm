package app.dsm.swarm.vo;

import lombok.Data;

/**
 * @ClassName : app.dsm.swarm.vo.BeeRegisterReqVO
 * @Description :
 * @Date 2021-08-19 13:29:42
 * @Author ZhangHL
 */
@Data
public class BeeRegisterReqVO {

    private String character;

    private String paths;

    private String approachIP;

    private String approachPort;

}
