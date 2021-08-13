package app.log;

import app.utils.SimpleUtils;


/**
 * @ClassName : utils.middle.wrapper.LogEntityWrapper
 * @Description :LogEntity包装器
 * @Date 2021-03-31 19:56:12
 * @Author ZhangHL
 */
public class LogEntityWrapper {

    public static  LogEntity normal(String src,String t){
        return new LogEntity(SimpleUtils.isEmptyString(src) ?"未知来源":src,t, LogAbnormalCode.NORMAL.getCode());
    }

    public static  LogEntity ok(String src,String t){
        return new LogEntity(SimpleUtils.isEmptyString(src) ?"未知来源":src,t, LogAbnormalCode.OK.getCode());
    }

    public static  LogEntity error(String src,String t){
        return new LogEntity(SimpleUtils.isEmptyString(src) ?"未知来源":src,t,LogAbnormalCode.ERROR.getCode());
    }

    public static  LogEntity unknown(String src,String t){
        return new LogEntity( SimpleUtils.isEmptyString(src) ?"未知来源":src,t,LogAbnormalCode.UNKNOWN.getCode());
    }
}
