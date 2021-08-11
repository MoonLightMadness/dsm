package app.utils.listener;

public interface IRegister {

    /**
     * 订阅服务
     * @param iListener 侦听器
     * @return
     * @author zhl
     * @date 2021-08-11 08:28
     * @version V1.0
     */
    void register(IListener iListener);

    /**
     * 取消订阅
     * @param iListener 侦听器
     * @return
     * @author zhl
     * @date 2021-08-11 08:29
     * @version V1.0
     */
    void cancle(IListener iListener);

    /**
     * 通知订阅者
     * @return
     * @author zhl
     * @date 2021-08-11 14:22
     * @version V1.0
     */
    void invoke();
}
