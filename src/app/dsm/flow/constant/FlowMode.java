package app.dsm.flow.constant;

public enum FlowMode {

    DEFAULT("01","default"),

    /**
     * 流程中因任何一流程发生错误则立即中断流程
     */
    IK("02","IK")

    ;




    FlowMode(String code, String message) {
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
