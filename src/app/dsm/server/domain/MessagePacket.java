package app.dsm.server.domain;

import lombok.Data;

@Data
public class MessagePacket {

    /**
     * 远程ip
     */
    private String remoteIP;

    /**
     * 远程端口
     */
    private String remotePort;

    /**
     * 数据
     */
    private byte[] data;




}
