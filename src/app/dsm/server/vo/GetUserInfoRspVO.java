package app.dsm.server.vo;

import lombok.Data;

@Data
public class GetUserInfoRspVO {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 身份验证级别
     */
    private String authLevel;


}
