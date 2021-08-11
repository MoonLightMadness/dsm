package app.utils.listener;

public interface ThreadListener extends Runnable,IListener{

    /**
     * 设置参数
     * @param obj  obj
     * @param args arg
     * @return
     * @author zhl
     * @date 2021-08-12 07:29
     * @version V1.0
     */
    public void setArgs(Object obj, String... args);

}
