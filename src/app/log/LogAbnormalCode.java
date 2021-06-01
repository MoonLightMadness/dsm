package app.log;

public enum LogAbnormalCode {

    OK("01","正常"),

    ERROR("02","发生错误"),

    NORMAL("03","正常"),

    UNKNOWN("99","未知错误"),


    ;
    LogAbnormalCode(String code, String message) {
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
