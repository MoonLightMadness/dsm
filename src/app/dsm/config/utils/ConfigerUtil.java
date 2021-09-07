package app.dsm.config.utils;

import app.dsm.config.Configer;
import app.log.LogSystem;
import app.log.LogSystemFactory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * @ClassName : app.dsm.config.utils.ConfigerUtil
 * @Description :
 * @Date 2021-09-06 09:32:11
 * @Author ZhangHL
 */
public class ConfigerUtil {

    private static Configer configer = new Configer();

    private static LogSystem log = LogSystemFactory.getLogSystem();

    /**
     * 获取具有Switch属性并判断值是否为On    <br/>
     * 例如某属性形式为switch = on 则返回true  <br/>
     * 又例如xxx.switch = false 则返回false   <br/>
     * 再例如 yyy = on 则返回true <br/>
     * 默认返回false    <br/>
     * @param property 属性名
     * @return @return boolean
     * @author zhl
     * @date 2021-09-06 09:32
     * @version V1.0
     */
    public static boolean getSwitchOn(String property){
        String value = configer.readConfig(property);
        if(value != null){
            if(value.toLowerCase(Locale.ROOT).equals("on")){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    /**
     * 判断配置文件中的具有Date属性的值是否是今天<br/>
     * 例如 xxx.date = 2021-01-01 而今天是2021-01-02,则返回false<br/>
     * 也支持DateTime类型的自动转换<br/>
     * 默认返回false<br/>
     * @param property 属性名
     * @return @return boolean
     * @author zhl
     * @date 2021-09-06 09:42
     * @version V1.0
     */
    public static boolean isToday(String property){
        String value = configer.readConfig(property);
        if(value != null){
            try {
                value = value.split("T")[0];
                LocalDate configDate = LocalDate.parse(value);
                LocalDate today = LocalDate.now();
                if(configDate.toString().equals(today.toString())){
                    return true;
                }
            }catch (DateTimeParseException e){
                log.error("时间转换失败，输入{}",value);
            }
        }
        return false;
    }


}
