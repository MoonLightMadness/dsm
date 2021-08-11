package app.dsm.server.adapter;

import lombok.Data;

import java.nio.channels.SocketChannel;

@Data
public class ListenerAdapter {

    /**
     * 数据
     */
    private byte[] data;

    /**
     * 远端服务器实体
     */
    private SocketChannel channel;
}
