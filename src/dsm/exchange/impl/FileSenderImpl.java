package dsm.exchange.impl;

import dsm.exchange.FileSender;
import dsm.utils.SimpleUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : dsm.exchange.impl.FileSenderImpl
 * @Description :
 * @Date 2021-05-12 16:03:59
 * @Author ZhangHL
 */
public class FileSenderImpl implements FileSender {
    @Override
    public void sendFile(String path, SocketChannel socketChannel) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return ;
            }
            FileInputStream fis = new FileInputStream(f);
            FileChannel channel = fis.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int size = 0, count = 0,last=0;
            while ((count = channel.read(buffer)) != -1) {
                buffer.flip();
                socketChannel.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
