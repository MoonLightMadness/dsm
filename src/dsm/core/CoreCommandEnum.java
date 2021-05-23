package dsm.core;

public enum CoreCommandEnum {

    SET_NAME("01","set_name"),

    ;





    CoreCommandEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
    private final String code;

    private final String message;



    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
