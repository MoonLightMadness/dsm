package app.dsm.server.constant;

public enum HttpEnum {

    /**
     * GET请求
     */
    GET("001","GET"),

    /**
     * POST请求
     */
    POST("002","POST"),

    /**
     * http
     */
    HTTP("003","HTTP"),

    ;

    private String code;
    private String msg;

    public String msg() {
        return msg;
    }

    public String code() {
        return code;
    }

    HttpEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static HttpEnum getEnum(int code) {
        for (HttpEnum ele : HttpEnum.values()) {
            if (ele.code() .equals(code) ) {
                return ele;
            }
        }
        return null;
    }
}
