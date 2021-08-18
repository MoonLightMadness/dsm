package app.dsm.server.domain;

import lombok.Data;

/**
 * @ClassName : app.dsm.server.domain.UserAuthData
 * @Description :
 * @Date 2021-08-18 08:43:16
 * @Author ZhangHL
 */
@Data
public class UserAuthData {

    private String userId;

    private String userPassword;

    private String authLevel;

}
