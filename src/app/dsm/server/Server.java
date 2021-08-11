package app.dsm.server;

import app.utils.listener.IListener;

public interface Server {


    /**
     * 初始化
     * @return
     * @author zhl
     * @date 2021-08-11 22:03
     * @version V1.0
     */
    void initialize();

    /**
     * 启动服务器
     * @return
     * @author zhl
     * @date 2021-08-11 20:37
     * @version V1.0
     */
    void open(IListener iListener);





}
