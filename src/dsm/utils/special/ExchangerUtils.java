package dsm.utils.special;

import dsm.base.BaseEntity;
import dsm.base.impl.UniversalEntity;
import dsm.security.EntitySecurityService;
import dsm.security.excp.DecodeException;
import dsm.security.excp.EncodeException;
import dsm.utils.SimpleUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : dsm.utils.special.ExchangerUtils
 * @Description :
 * @Date 2021-05-06 21:24:44
 * @Author ZhangHL
 */
public class ExchangerUtils {

    public static void Send(SocketChannel socketChannel,String ak, UniversalEntity entity, EntitySecurityService securityService){
        try {
            byte[] bytes=null ;
            if(entity.getCompressCode().equals("0")){
                bytes=SimpleUtils.serializableToBytes(entity);
            }else {
                bytes= SimpleUtils.serializableToBytes(securityService.encode(entity,ak));
            }
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            socketChannel.write(buffer);
        } catch (EncodeException | IOException e) {
            e.printStackTrace();
        }
    }

    public static BaseEntity Receive(SocketChannel socketChannel,String ak, UniversalEntity entity, EntitySecurityService securityService){
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int size = -1;
            int count = 0;
            byte[] temp;
            List<byte[]> recv=new ArrayList<>();
            while (true) {
                buffer.clear();
                try {
                    size = socketChannel.read(buffer);
                    count += size;
                } catch (Exception e) {
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
            byte[] data = mergeBytesList(count,recv);
            recv.clear();
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            entity = (UniversalEntity) ois.readObject();
            entity= (UniversalEntity) securityService.decode(entity,entity.getCompressCode());
            return entity;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (DecodeException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] mergeBytesList(int count,List<byte[]> recv) {
        byte[] res = new byte[count];
        int pointer = 0;
        for (byte[] b : recv) {
            if (pointer >= count) {
                break;
            }
            System.arraycopy(b, 0, res, pointer, b.length);
            pointer += b.length;
        }
        return res;
    }
}
