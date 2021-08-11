package app.dsm.server.container;

import lombok.Data;

import java.nio.channels.SocketChannel;

@Data
public class ServerEntity {

    /**
     * 套接字通道
     */
    private SocketChannel socketChannel;

    /**
     * 服务器id
     */
    private String id = "-1";

    /**
     * 服务器的名字
     */
    private String name = "default";

    /**
     * 心跳
     */
    private long beat = 0;

}
