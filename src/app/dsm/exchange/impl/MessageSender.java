package app.dsm.exchange.impl;

import app.dsm.exchange.Sender;
import app.dsm.log.LogSystem;
import app.dsm.log.LogSystemFactory;
import app.dsm.security.SecurityService;
import app.dsm.security.excp.EncodeException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * 消息发送器
 * @ClassName : app.dsm.exchange.impl.MessageSender
 * @Description :
 * @Date 2021-05-01 20:34:08
 * @Author ZhangHL
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
