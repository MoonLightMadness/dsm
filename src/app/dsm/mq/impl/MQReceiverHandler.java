package app.dsm.mq.impl;

import app.dsm.base.BaseEntity;
import app.dsm.base.impl.UniversalEntity;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.dsm.mq.entity.Consumer;
import app.utils.net.impl.CallBack;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * @ClassName : app.dsm.mq.impl.MQReceiverHandler
 * @Description :
 * @Date 2021-05-28 14:11:10
 * @Author ZhangHL
 */
public class MQReceiverHandler extends CallBack {

    private List<BaseEntity> list;

    private LogSystem log;

    private List<Consumer> consumers;

    private MQSender sender;

    public void init(List list) {
        this.list = list;
        log = LogSystemFactory.getLogSystem();
        consumers = new ArrayList<>();
        sender=new MQSender();
        sender.init(list,consumers,1);
        new Thread(sender).start();
    }


    @Override
    public void invoke(SocketChannel channel, BaseEntity entity) {
        UniversalEntity uni = (UniversalEntity) entity;
        //消费者注册
        if(uni.getMessage().startsWith("register")){
            register(channel, uni.getMessage());
        }
        //添加消息
        if(uni.getMessage().startsWith("message")){
            ListIterator iter = list.listIterator();
            iter.add(entity);
        }

    }

    private void register(SocketChannel channel,String data){
        try {
            //检查是否是重新再注册，是的话刷新兴趣表
            for(Consumer c:consumers){
                if(c.getChannel().isConnected()){
                    if(c.getChannel().getRemoteAddress().toString().equals(channel.getRemoteAddress().toString())){
                        c.setInterest(data);
                        return;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String interest = data.split(" ")[1];
        Consumer con = new Consumer();
        con.setChannel(channel);
        con.setInterest(interest);
        ListIterator iter = consumers.listIterator();
        iter.add(con);
    }
}
