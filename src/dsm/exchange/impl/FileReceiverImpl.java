package dsm.exchange.impl;

import dsm.exchange.FileReceiver;
import dsm.log.LogSystem;
import dsm.log.LogSystemFactory;
import dsm.utils.SimpleUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : dsm.exchange.impl.FileReceiverImpl
 * @Description :
 * @Date 2021-05-12 16:09:59
 * @Author ZhangHL
 */
public class FileReceiverImpl implements FileReceiver {

    private LogSystem log;

    /**
     * 接收文件并保存，返回文件字节数组
     *
     * @param socketChannel 套接字通道
     * @param name          的名字
     * @return {@link byte[]}
     */
    @Override
    public byte[] receive(SocketChannel socketChannel,String name) {
        log = LogSystemFactory.getLogSystem();
        List<byte[]> recv = new ArrayList<>();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int size = 0;
        int count = 0;
        byte[] temp;
        while (true) {
            buffer.clear();
            try {
                size = socketChannel.read(buffer);
                count += size;
            } catch (Exception e) {
                log.info(this.getClass().getName(), "发生错误--{}", e.toString());
                try {
                    socketChannel.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                break;
            }
            if (size <= 0) {
                break;
            }
            buffer.flip();
            temp = new byte[size];
            System.arraycopy(buffer.array(), 0, temp, 0, size);
            recv.add(temp);
        }
        /**
         * 预处理接收到的信息，并做后续处理
         */
        byte[] data = SimpleUtils.mergeByteList(recv,count);
        File f = new File(name);
        try {
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
