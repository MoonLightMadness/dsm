package app.dsm.server.console;

import app.log.LogSystem;
import app.log.LogSystemFactory;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 控制台
 * 可通过此控制台连接至服务器
 * @author zhl
 * @date 2021-08-21 20:23
 */
public class Console {


   String ip;

   String port;

   LogSystem log = LogSystemFactory.getLogSystem();

    /**
     * 套接字通道
     */
    SocketChannel socketChannel;

   public Console(String ip,String port){
       this.ip = ip;
       this.port = port;
       try {
           socketChannel = SocketChannel.open(new InetSocketAddress(ip,Integer.parseInt(port)));
           socketChannel.configureBlocking(false);
           log.info("已成功连接至服务器");
       }catch (Exception e) {
           log.error("初始化套接字通道失败，原因:{}",e);
       }
   }

}
