package app.dsm.exchange.impl;

import app.dsm.exchange.FileSender;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName : app.dsm.exchange.impl.FileSenderImpl
 * @Description :
 * @Date 2021-05-12 16:03:59
 * @Author ZhangHL
 */
public class FileSenderImpl implements FileSender {

    private final List<ByteBuffer> buffers = new ArrayList<>();

    private final int buffersize=1024*100;

    private int pointer = 0;
    @Override
    public void sendFile(String path, SocketChannel socketChannel) {
        readFile(path);
        try {
            Selector selector = Selector.open();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_WRITE);
            int w =0;
            while (pointer<buffers.size()){
                int num = selector.select();
                Iterator iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey sk = (SelectionKey) iterator.next();
                    if(sk.isWritable()){
                        w+=((SocketChannel)sk.channel()).write(buffers.get(pointer));
                        pointer++;
                    }
                    iterator.remove();
                }
            }
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readFile(String path){
        try {
            File f = new File(path);
            if (!f.exists()) {
                return ;
            }
            FileInputStream fis = new FileInputStream(f);
            FileChannel channel = fis.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(buffersize);
            int size = 0, count = 0,last=0;
            while ((count = channel.read(buffer)) != -1) {
                size+=count;
                buffer.flip();
                buffers.add(buffer);
                buffer = ByteBuffer.allocate(buffersize);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
