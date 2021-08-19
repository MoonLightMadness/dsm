package app.dsm.swarm.enums;

import app.dsm.server.constant.AuthorityEnum;

public enum CharacterEnum {

    /**
     * 蜂后
     */
    QUEEN("001","QUEEN"),

    /**
     * 日志
     */
    LOGGER("002","LOGGER"),

    /**
     * 端口
     */
    PORTAL("003","PORTAL"),

    /**
     * 工人
     */
    WORKER("004","WORKER")



    ;

    private String code;
    private String msg;

    public String msg() {
        return msg;
    }

    public String code() {
        return code;
    }

    CharacterEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static CharacterEnum getEnum(int code) {
        for (CharacterEnum ele : CharacterEnum.values()) {
            if (ele.code() .equals(code) ) {
                return ele;
            }
        }
        return null;
    }

    public static CharacterEnum getByMsg(String msg){
        for (CharacterEnum ae : CharacterEnum.values()){
            if(ae.msg.equals(msg)){
                return ae;
            }
        }
        return null;
    }
}
