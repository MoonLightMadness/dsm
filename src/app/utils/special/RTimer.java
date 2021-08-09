package app.utils.special;

/**
 * 用于记录程序运行时间
 *
 * @author ZhangHL
 * @version 1.0.0
 * @date 2021/06/01
 */
public class RTimer {
    private long time = 0;

    private long total = 0;
    /**
     * 开始 <br\>
     * 会重置时间
     */
    public void start(){
        time = System.currentTimeMillis();
    }

    /**
     * 结束
     *
     * @return long 运行时间
     */
    public long end(){
        long interval = System.currentTimeMillis() - time;
        total += interval;
        return interval;
    }

    /**
     * 返回所有start()->end()调用的时间总和
     *
     * @return long
     */
    public long getTotal(){
        return total;
    }


    /**
     * 清除总计数
     */
    public  void clear(){
        total = 0;
    }
}
