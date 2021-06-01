package app.dsm.flow.constant;

public enum FlowStatusEnum {
    READY("01","Ready"),

    RUNNING("02","Running"),

    PREPARE("03","Prepare"),

    ABORT("04","Abort"),

    SUSPEND("05","Suspend"),

    DESUSPEND("06","Desuspend"),

    CLOSED("07","Closed");

    FlowStatusEnum(String code, String message) {
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
