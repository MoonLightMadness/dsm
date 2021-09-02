package app.dsm.server.adapter;

import app.dsm.server.container.ServerEntity;
import app.dsm.server.domain.HttpEntity;
import app.dsm.server.domain.MessagePacket;
import app.dsm.server.domain.ModeSwitcher;
import app.dsm.server.exception.ServerException;
import app.dsm.server.handler.Handler;
import app.dsm.server.handler.HandlerFactory;
import app.dsm.server.http.HttpParser;
import app.dsm.server.impl.SelectorIOImpl;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.utils.SimpleUtils;
import app.utils.special.RTimer;
import lombok.Data;
import lombok.SneakyThrows;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

@Data
public class ListenerAdapter implements Adapter {


    /**
     * 消息包
     */
    private MessagePacket messagePacket;

    /**
     * 远端服务器实体
     */
    private SocketChannel channel;

    /**
     * 数据选择器
     */
    private SelectorIOImpl selectorIO;



    private LogSystem log = LogSystemFactory.getLogSystem();

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        log.info("进入ListenerAdapter线程,开始计时");
        RTimer rTimer = new RTimer();
        rTimer.start();
        //重置心跳
        List<ServerEntity> list = selectorIO.getServerContainer().getServers();
        for (ServerEntity entity : list) {
            if (entity.getSocketChannel() == channel) {
                entity.setBeat(0L);
                log.info("心跳重置成功");
                break;
            }
        }
        try {
            log.info("正在接收数据");
            messagePacket = new MessagePacket();
            String[] address = SimpleUtils.addressCutter(channel.getRemoteAddress().toString());
            messagePacket.setRemoteIP(address[0]);
            messagePacket.setRemotePort(address[1]);
            messagePacket.setData(SimpleUtils.receiveDataInNIO(channel));
            //接收完毕将该channel从集合中移除
            removeFromReceiving(channel);
        } catch (Exception e) {
            log.error("接收数据失败，原因：{}", e);
        }

        if (null != messagePacket.getData() && messagePacket.getData().length > 0) {
            //数据再组装
            messagePacket.setData(reConstruct(messagePacket.getData()));
            switcher();
        } else {
            log.error("收到无效数据");
            try {
                channel.close();
            } catch (IOException e) {
                log.error("关闭连接异常,原因;{}",e);
            }
        }
        log.info("ListenerAdapter线程结束，环节计时:{}",rTimer.end());
    }

    /**
     * 选择器
     * 选择执行何种策略来处理接收的数据
     * @return
     * @author zhl
     * @date 2021-08-21 23:35
     * @version V1.0
     */
    @SneakyThrows
    private void switcher(){
        log.info("选择器开始执行");
        ModeSwitcher modeSwitcher = (ModeSwitcher) SimpleUtils.parseTo(messagePacket.getData(), ModeSwitcher.class);
        if(null == modeSwitcher.getSwitcher()){
            throw new ServerException("字段switcher不能为空");
        }
        //调用处理器进行处理
        Handler handler = HandlerFactory.getHandler(modeSwitcher.getSwitcher().toUpperCase(Locale.ROOT));
        handler.handle(this);
    }




    private void removeFromReceiving(SocketChannel socketChannel){
        List<SocketChannel> list = selectorIO.getReceivingChannels();
        ListIterator<SocketChannel> iterator = list.listIterator();
        while (iterator.hasNext()){
            SocketChannel temp = iterator.next();
            if(temp == socketChannel){
                iterator.remove();
                return;
            }
        }
    }

    private byte[] reConstruct(byte[] data){
        String sdata = new String(data);
        HttpEntity entity = HttpParser.parse(sdata);
        StringBuilder sb = new StringBuilder();
        String removeBracedStr = null;
        if(null!=entity.getBody()){
            removeBracedStr = entity.getBody().substring(1,entity.getBody().length()-1);
            sb.append("{").append("\n");
            sb.append(removeBracedStr).append("\t,").append("\n");
            sb.append("\t\"path\":").append("\"").append(entity.getRequestPath()).append("\"").append("\n");
            sb.append("}");
        }else {
            sb.append("{").append("\n");
            sb.append("\t\"path\":").append("\"").append(entity.getRequestPath()).append("\"").append("\n");
            sb.append("}");
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);

    }
}
