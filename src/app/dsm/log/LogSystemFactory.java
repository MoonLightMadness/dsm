package app.dsm.log;

/**
 * @ClassName : app.dsm.log.LogSystemFactory
 * @Description :
 * @Date 2021-04-29 22:33:03
 * @Author ZhangHL
 */
public class LogSystemFactory {

    private static LogSystem logSystem;

    /**
     * 获取logSystem实例
     * @class
     * @Param
     * @return
     * @Author Zhang huai lan
     * @Date 22:36 2021/4/29
     * @Version V1.0
     **/
    public static LogSystem getLogSystem(){
        if(logSystem==null){
            logSystem=new LogSystem();
            logSystem.init();
            logSystem.immediatelySaveMode(true);
        }
        return logSystem;
    }
}
