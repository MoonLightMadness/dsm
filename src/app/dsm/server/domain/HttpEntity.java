package app.dsm.server.domain;

import lombok.Data;

/**
 * @ClassName : app.dsm.server.domain.HttpEntity
 * @Description :
 * @Date 2021-08-16 08:12:58
 * @Author ZhangHL
 */
@Data
public class HttpEntity {

    /**
     * 请求模式(POST,GET,etc.)
     */
    private String mode;

    /**
     * 协议版本(HTTP/1.1,etc.)
     */
    private String protocol;

    /**
     * 请求路径
     */
    private String requestPath;

    /**
     * 数据体
     */
    private String body;

}
