package app.dsm.server;

import java.nio.channels.SocketChannel;

public interface DataIO {

    /**
     * 接收数据
     * @return @return {@link byte[] }
     * @author zhl
     * @date 2021-08-11 20:39
     * @version V1.0
     */
    byte[] receiveData(SocketChannel socketChannel);


    /**
     * 发送数据
     * @param data          数据
     * @param socketChannel 套接字通道
     * @return
     * @author zhl
     * @date 2021-08-11 20:40
     * @version V1.0
     */
    void sendData(byte[] data, SocketChannel socketChannel);

}
