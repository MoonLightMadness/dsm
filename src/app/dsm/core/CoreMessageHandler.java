package app.dsm.core;

import app.dsm.base.BaseEntity;
import app.dsm.base.impl.UniversalEntity;
import app.dsm.base.impl.UniversalEntityWrapper;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.utils.SimpleUtils;
import app.utils.net.Sender;
import app.utils.net.impl.CallBack;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class CoreMessageHandler extends CallBack {

    private List<ChannelInfo> list;

    private LogSystem log;

    public void init(List<ChannelInfo> list) {
        this.list = list;
        log = LogSystemFactory.getLogSystem();
    }

    @Override
    public void invoke(SocketChannel channel, BaseEntity entity) {
        //log.info(null,"开始处理");
        UniversalEntity universalEntity = (UniversalEntity) entity;
        String cmd = universalEntity.getMessage();
        cmd = cmd.toLowerCase(Locale.ROOT);
        if(cmd.startsWith(CoreCommandEnum.SET_NAME.getMessage())){
            log.info(null,"接收到更名指令");
            resetName(channel,universalEntity.getMessage().split(" ")[1],universalEntity.getMessage().split(" ")[2]);
        }
        if(cmd.startsWith(CoreCommandEnum.GET_IP.getMessage())){
            log.info(null, "返回ip");
            UniversalEntity ip = (UniversalEntity) getIP(cmd.split(" ")[1]);
            send(channel,ip);
        }
        if(universalEntity.getMessageType().toLowerCase(Locale.ROOT).equals(CoreCommandEnum.BEAT.getMessage())){
            checkBeat(channel,universalEntity);
        }
    }

    /**
     * 生成ChannelInfo并加入到list中
     *
     * @param channel 通道
     */
    private void generateInfo(SocketChannel channel){
        try {
            channel.configureBlocking(false);
            ChannelInfo info = new ChannelInfo();
            info.setChannel(channel);
            ListIterator<ChannelInfo> iterator = list.listIterator();
            iterator.add(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetName(SocketChannel channel,String name,String ip){
        ListIterator<ChannelInfo> iterator = list.listIterator();
        try {
            synchronized (iterator){
                while (iterator.hasNext()){
                    ChannelInfo info = iterator.next();
                    if(info.getChannel().getRemoteAddress().toString().equals(channel.getRemoteAddress().toString())){
                        info.setName(name);
                        info.setIp(ip);
                        log.info(null,"reset_name:{}",name);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            log.error(null,e.getMessage());
        }
    }

    private BaseEntity getIP(String name){
        ListIterator<ChannelInfo> iterator = list.listIterator();
        String ip=null;
        UniversalEntity entity=null;
        synchronized (iterator){
            while (iterator.hasNext()){
                ChannelInfo info = iterator.next();
                if(info.getName()!=null&&info.getName().equals(name)){
                    ip = info.getIp();
                    break;
                }
            }
        }
        entity = UniversalEntityWrapper.getOne("0",
                "1","core","remote","1",ip,"reply","00001");
        return entity;
    }

    private void send(SocketChannel channel,BaseEntity entity){
        byte[] data = SimpleUtils.serializableToBytes(entity);
        Sender.send(channel,data);
        log.info(null,"已发送:{}",entity.toString());
    }

    private void checkBeat(SocketChannel channel,UniversalEntity entity){
        ListIterator listIterator = list.listIterator();
        try {
            while (listIterator.hasNext()){
                ChannelInfo info = (ChannelInfo) listIterator.next();
                if(info.getChannel().isConnected()){
                    if(info.getChannel().getRemoteAddress().toString().equals(channel.getRemoteAddress().toString())){
                        info.reset();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
