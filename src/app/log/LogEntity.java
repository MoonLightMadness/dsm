package app.log;

import app.utils.SimpleUtils;


/**
 * 日志POJO
 * @ClassName : utils.middle.entity.LogEntity
 * @Description :日志POJO
 * @Date 2021-03-31 19:26:41
 * @Author ZhangHL
 */
public class LogEntity<T>  {
    private  String timestamp=null;

    private  String source=null;

    private  String signCode=null;

    private  T message=null;

    public LogEntity(String source,T message,String signCode){
        this.source=source;
        this.message=message;
        this.signCode=signCode;
        timestamp= SimpleUtils.getTimeStamp();
    }


    @Override
    public String toString() {
        return "LogEntity{" +
                "timestamp='" + timestamp + '\'' +
                ", source='" + source + '\'' +
                ", signCode='" + signCode + '\'' +
                ", message='" + message.toString() + '\'' +
                '}';
    }


}
