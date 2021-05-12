package dsm.exchange;

import java.nio.channels.SocketChannel;

/**
 * 文件发送
 *
 * @author ZhangHL
 * @version 1.0.0
 * @date 2021/05/12
 */
public interface FileSender {

    /**
     * 发送文件
     *
     * @param path          文件
     * @param socketChannel 套接字通道
     */
    public void sendFile(String path, SocketChannel socketChannel);

}
