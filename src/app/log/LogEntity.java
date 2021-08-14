package app.log;

import app.utils.SimpleUtils;
import lombok.Data;


/**
 * 日志POJO
 * @ClassName : utils.middle.entity.LogEntity
 * @Description :日志POJO
 * @Date 2021-03-31 19:26:41
 * @Author ZhangHL
 */
@Data
public class LogEntity  {
    private  String timestamp=null;

    private  String source=null;

    private  String signCode=null;

    private  String message=null;

    public LogEntity(String source,String message,String signCode){
        this.source=source;
        this.message=message;
        this.signCode=signCode;
        timestamp= SimpleUtils.getTimeStamp();
    }



}
