package app.dsm.server.domain;

import lombok.Data;

@Data
public class BasePath {

    private String path;

    /**
     * 响应
     * 0-不触发方法 1-触发方法
     */
    private String response;

}
