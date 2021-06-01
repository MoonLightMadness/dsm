package app.utils.special;

/**
 * 用于记录程序运行时间
 *
 * @author ZhangHL
 * @version 1.0.0
 * @date 2021/06/01
 */
public class RTimer {
    private long time = 1;

    public void start(){
        time = System.currentTimeMillis();
    }

    public long end(){
        return System.currentTimeMillis() - time;
    }

}
