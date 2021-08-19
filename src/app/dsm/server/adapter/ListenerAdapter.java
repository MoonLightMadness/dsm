package app.dsm.server.adapter;

import app.dsm.base.JSONTool;
import app.dsm.server.container.ServerEntity;
import app.dsm.server.domain.HttpEntity;
import app.dsm.server.http.HttpParser;
import app.dsm.server.impl.SelectorIOImpl;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.utils.SimpleUtils;
import app.utils.listener.ThreadListener;
import app.utils.special.RTimer;
import lombok.Data;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ListIterator;

@Data
public class ListenerAdapter implements Runnable {

    /**
     * 数据
     */
    private byte[] data;

    /**
     * 远端服务器实体
     */
    private SocketChannel channel;

    /**
     * 数据选择器
     */
    private SelectorIOImpl selectorIO;

    /**
     * 线程监听器
     */
    private ThreadListener threadListener;

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
            data = SimpleUtils.receiveDataInNIO(channel);
            //接收完毕将该channel从集合中移除
            removeFromReceiving(channel);
        } catch (Exception e) {
            log.error("接收数据失败，原因：{}", e);
        }
        if (null == threadListener) {
            log.error("未指定订阅方法,触发事件结束");
            return;
        }

        if (null != data && data.length > 0) {
            //数据再组装
            System.out.println(new String(data)+" "+data.length);
            data = reConstruct(data);

            log.info("异步接收数据完成，开始触发订阅方法");
            try {
                threadListener.setArgs(this);
                ((ApiListenerAdapter) threadListener).setListenerAdapter(this);
                threadListener.invoke(this);
                log.info("订阅方法触发完成");
            } catch (Exception e) {
                log.error("订阅方法执行失败，原因：{}", e);
                e.printStackTrace();
            }
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
            sb.append(removeBracedStr).append(",").append("\n");
            sb.append("\"path\":").append("\"").append(entity.getRequestPath()).append("\"").append("\n");
            sb.append("}");
        }else {
            sb.append("{").append("\n");
            sb.append("\"path\":").append("\"").append(entity.getRequestPath()).append("\"").append("\n");
            sb.append("}");
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);

    }
}
