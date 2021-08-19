package app.dsm.server;

import app.utils.listener.IListener;
import app.utils.listener.ThreadListener;

public interface Server {


    /**
     * 初始化
     * @return
     * @author zhl
     * @date 2021-08-11 22:03
     * @version V1.0
     */
    void initialize(String ip,String port);

    /**
     * 启动服务器
     * @return
     * @author zhl
     * @date 2021-08-11 20:37
     * @version V1.0
     */
    void open();

    /**
     * @return @return {@link SelectorIO }
     * @author zhl
     * @date 2021-08-19 10:22
     * @version V1.0
     */
    SelectorIO getSelectorIo();





}
