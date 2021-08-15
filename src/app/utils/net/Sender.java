package app.utils.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @ClassName : app.utils.net.Sender
 * @Description :
 * @Date 2021-05-14 09:00:15
 * @Author ZhangHL
 */
public class Sender {
    public static void send(SocketChannel channel, byte[] data){
        try {
            channel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.put(data);
        buffer.flip();
        try {
            channel.write(buffer);
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
