package app.dsm.server.constant;

public enum AuthorityEnum {

    /**
     * 普通级别
     */
    NORMAL("001","NORMAL"),

    /**
     * 授权级别
     */
    HIGH("002","HIGH")


    ;

    private String code;
    private String msg;

    public String msg() {
        return msg;
    }

    public String code() {
        return code;
    }

    AuthorityEnum(String code, String msg) {
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
