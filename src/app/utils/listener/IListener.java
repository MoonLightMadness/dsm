package app.utils.listener;

public interface IListener {

    /**
     * 触发动作
     * @return
     * @author zhl
     * @date 2021-08-11 08:29
     * @version V1.0
     */
    void invoke(Object obj,String... args);

}
