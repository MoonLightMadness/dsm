package dsm.core;

import dsm.base.BaseEntity;
import dsm.base.impl.UniversalEntity;
import dsm.log.LogSystem;
import dsm.log.LogSystemFactory;
import dsm.utils.net.impl.CallBack;

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
        log.info(null,"开始处理");
        UniversalEntity universalEntity = (UniversalEntity) entity;
        String cmd = universalEntity.getMessage();
        cmd = cmd.toLowerCase(Locale.ROOT);
        try {
            if(cmd.startsWith(CoreCommandEnum.SET_NAME.getMessage())){
                log.info(null,"接收到更名指令");
                resetName(universalEntity.getMessage().split(" ")[1],channel.getRemoteAddress().toString());
            }
        } catch (IOException e) {
            log.error(null,e.getMessage());
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

    private void resetName(String name,String ip){
        ListIterator<ChannelInfo> iterator = list.listIterator();
        try {
            synchronized (iterator){
                while (iterator.hasNext()){
                    ChannelInfo info = iterator.next();
                    if(info.getChannel().getRemoteAddress().toString().equals(ip)){
                        info.setName(name);
                        log.info(null,"reset_name:{}",name);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            log.error(null,e.getMessage());
        }
    }
}
