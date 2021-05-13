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
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName : dsm.exchange.impl.FileReceiverImpl
 * @Description :
 * @Date 2021-05-12 16:09:59
 * @Author ZhangHL
 */
public class FileReceiverImpl implements FileReceiver {

    private final List<ByteBuffer> buffers = new ArrayList<>();

    private final List<Integer> counts = new ArrayList<>();

    private int count=0,size=0;
    /**
     * 接收文件并保存，返回文件字节数组
     *
     * @param channel 套接字通道
     * @param name          的名字
     * @return {@link byte[]}
     */
    @Override
    public byte[] receive(SocketChannel channel,String name){
        int pointer = 0,nullcount=0;
        try {
            Selector selector = Selector.open();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
            while (nullcount<30){
                int num = selector.select(100);
                if(num==0){
                    nullcount++;
                }else {
                    Iterator iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey sk = (SelectionKey) iterator.next();
                        if(sk.isReadable()){
                            ByteBuffer buffer = ByteBuffer.allocate(1024*100);
                            count=((SocketChannel)sk.channel()).read(buffer);
                            counts.add(count);
                            size+=count;
                            buffer.flip();
                            buffers.add(buffer);
                        }
                        iterator.remove();
                    }
                }
            }
            byte[] data = convert();
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

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] convert(){
        byte[] data = new byte[size];
        int p=0,index=0;
        for(ByteBuffer buffer:buffers){
            byte[] temp = buffer.array();
            System.arraycopy(temp,0,data,p,counts.get(index));
            p+= counts.get(index);
            index++;
        }
        return data;
    }
}
