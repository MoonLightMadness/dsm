package test;

import dsm.base.impl.UniversalEntity;
import dsm.base.impl.UniversalEntityWrapper;
import dsm.core.Core;
import dsm.utils.EntityUtils;
import dsm.utils.SimpleUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class core {

    @Test
    public void test1() throws IOException {
        Core core = new Core();
        core.init();
        Thread thread = new Thread(core);
        thread.start();
        try {
            while (true){

                SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(InetAddress.getLocalHost(),9002));
                Thread.sleep(400);
                UniversalEntity entity = UniversalEntityWrapper.getOne(String.valueOf(new Date().getTime()),
                        "1",
                        "test",
                        "core",
                        "1",
                        "set_name "+String.valueOf(new Date().getTime()),
                        "null",
                        "00001");
                byte[] data = SimpleUtils.serializableToBytes(entity);
                ByteBuffer buffer = ByteBuffer.allocate(data.length);
                buffer.put(data);
                buffer.flip();
                socketChannel.write(buffer);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
