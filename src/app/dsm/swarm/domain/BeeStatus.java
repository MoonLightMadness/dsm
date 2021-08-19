package app.dsm.swarm.domain;

import lombok.Data;

/**
 * @ClassName : app.dsm.swarm.domain.BeeStatus
 * @Description :
 * @Date 2021-08-19 09:40:03
 * @Author ZhangHL
 */
@Data
public class BeeStatus {

    private String character;

    private String status;

    private String updateTime;

    private String remoteIp;

    private String remotePort;


}
