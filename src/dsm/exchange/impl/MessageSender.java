package dsm.exchange.impl;

import dsm.config.impl.IPConfigReader;
import dsm.exchange.Sender;
import dsm.log.LogSystem;
import dsm.log.LogSystemFactory;
import dsm.security.SecurityService;
import dsm.security.excp.EncodeException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * 消息发送器
 * @ClassName : dsm.exchange.impl.MessageSender
 * @Description :
 * @Date 2021-05-01 20:34:08
 * @Author 张怀栏
 */
public class MessageSender implements Sender {

    private SocketChannel socketChannel;

    private SecurityService securityService;

    private LogSystem log;



    public void init(SocketChannel socketChannel,SecurityService securityService,String key){
        this.socketChannel=socketChannel;
        this.securityService=securityService;
        this.log= LogSystemFactory.getLogSystem();
    }

    public void init(SocketChannel socketChannel,SecurityService securityService){
        this.socketChannel=socketChannel;
        this.securityService=securityService;
        this.log= LogSystemFactory.getLogSystem();
    }
    @Override
    public void send(String text) {
       try {
           text = securityService.encode(text);
           ByteBuffer buffer=ByteBuffer.allocate(text.length());
           buffer.put(text.getBytes(StandardCharsets.UTF_8));
           buffer.flip();
           socketChannel.write(buffer);
       }catch (EncodeException | IOException ee){
           log.error(this.getClass().getCanonicalName(),ee.getMessage());
       }
    }


}
