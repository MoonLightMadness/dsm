package app.dsm.server.vo;

import lombok.Data;

@Data
public class UserAuthReqDTO {
    private String id;

    private String userName;

    private String userId;

    private String userPassword;

    private String authLevel;

    private String createTime;
}
