package app.dsm.exchange;

import java.nio.channels.SocketChannel;

/**
 * 文件接收
 *
 * @author ZhangHL
 * @version 1.0.0
 * @date 2021/05/12
 */
public interface FileReceiver {
    /**
     * 接收文件并保存，返回文件字节数组
     *
     * @param channel 套接字通道
     * @param name          的名字
     * @return {@link byte[]}
     */
    byte[] receive(SocketChannel channel, String name);

}
