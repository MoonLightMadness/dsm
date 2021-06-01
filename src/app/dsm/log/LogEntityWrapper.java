package app.dsm.log;

import app.dsm.utils.SimpleUtils;


/**
 * @ClassName : utils.middle.wrapper.LogEntityWrapper
 * @Description :LogEntity包装器
 * @Date 2021-03-31 19:56:12
 * @Author ZhangHL
 */
public class LogEntityWrapper {

    public static <T> LogEntity<T> normal(String src,T t){
        return new LogEntity<T>(SimpleUtils.isEmptyString(src) ?"未知来源":src,t, LogAbnormalCode.NORMAL.getCode());
    }

    public static <T> LogEntity<T> ok(String src,T t){
        return new LogEntity<T>(SimpleUtils.isEmptyString(src) ?"未知来源":src,t, LogAbnormalCode.OK.getCode());
    }

    public static <T> LogEntity<T> error(String src,T t){
        return new LogEntity<T>(SimpleUtils.isEmptyString(src) ?"未知来源":src,t,LogAbnormalCode.ERROR.getCode());
    }

    public static <T> LogEntity<T> unknown(String src,T t){
        return new LogEntity<T>( SimpleUtils.isEmptyString(src) ?"未知来源":src,t,LogAbnormalCode.UNKNOWN.getCode());
    }
}
