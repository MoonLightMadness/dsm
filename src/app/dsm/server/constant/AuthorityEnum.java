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

    public static AuthorityEnum getEnum(int code) {
        for (AuthorityEnum ele : AuthorityEnum.values()) {
            if (ele.code() .equals(code) ) {
                return ele;
            }
        }
        return null;
    }

    public static AuthorityEnum getByMsg(String msg){
        for (AuthorityEnum ae : AuthorityEnum.values()){
            if(ae.msg.equals(msg)){
                return ae;
            }
        }
        return null;
    }
}
