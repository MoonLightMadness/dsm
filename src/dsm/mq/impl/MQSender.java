package dsm.mq.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @ClassName : dsm.mq.impl.MQSender
 * @Description :
 * @Date 2021-05-13 14:57:24
 * @Author ZhangHL
 */
public class MQSender {
    public static void send(SocketChannel channel,byte[] data){
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.put(data);
        buffer.flip();
        try {
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
