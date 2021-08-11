package app.dsm.server.domain;

import lombok.Data;

import java.nio.channels.SocketChannel;

@Data
public class ServerEntityBeatDTO {

    private SocketChannel socketChannel;

    private int beat = 0;

    private String name;
}
