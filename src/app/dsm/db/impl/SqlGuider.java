package app.dsm.db.impl;

/**
 * Sql语句映射表
 * @author Administrator
 */

public enum SqlGuider {
    /**
     * 测试
     * @param
     * @author Zhang huai lan
     * @version V1.0
     **/
    INSERTTEST("99999","insert"),
    GETTEST("99998","get"),

    ;




    SqlGuider(String code, String message) {
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
