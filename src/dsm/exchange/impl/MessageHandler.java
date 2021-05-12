package dsm.exchange.impl;

import dsm.base.impl.UniversalEntity;
import dsm.base.impl.UniversalEntityWrapper;
import dsm.container.Container;
import dsm.exchange.Handler;
import dsm.exchange.constant.MessageHandlerConstant;
import dsm.log.LogSystem;
import dsm.log.LogSystemFactory;
import dsm.security.EntitySecurityService;
import dsm.security.excp.EncodeException;
import dsm.utils.EntityUtils;
import dsm.utils.SimpleUtils;
import dsm.utils.special.DBUtils;
import dsm.utils.special.ExchangerUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Locale;

/**
 * 内置消息处理器
 * @ClassName : dsm.exchange.impl.MessageHandler
 * @Description :
 * @Date 2021-05-01 22:10:01
 * @author ZhangHL
 */
public class MessageHandler implements Handler {
    private SelectionKey sk;

    private UniversalEntity entity;

    private EntitySecurityService securityService;

    private Container<SocketChannel> container;

    private LogSystem log;


    public void init(SelectionKey sk,
                     UniversalEntity entity,
                     EntitySecurityService securityService,
                     Container<SocketChannel> container){
        this.sk=sk;
        this.entity=entity;
        this.securityService=securityService;
        this.container=container;
        log= LogSystemFactory.getLogSystem();
    }
    @Override
    public void handle() {
        switch (entity.getAuthLevel().toLowerCase(Locale.ROOT)){
            case MessageHandlerConstant.SERVICE:
                registerService();
                break;
            case MessageHandlerConstant.GET_LIST:
                getServiceList();
                break;
            default:
                returnError();
        }
    }

    private void registerService(){
        if(entity.getSrc().equals(DBUtils.getServiceNameByAK(entity.getCompressCode()))){
            try {
                if(entity.getSrc().equals(DBUtils.getServiceNameByAK(entity.getCompressCode()))){
                    log.info(this.getClass().getName(),"消息解析为[服务注册]");
                    container.reName(((SocketChannel)sk.channel()).getRemoteAddress().toString(),entity.getSrc());
                    UniversalEntity ok= UniversalEntityWrapper.getOKEntity(entity.getCompressCode());
                    log.info(this.getClass().getName(),"返回成功消息");
                    ExchangerUtils.Send(((SocketChannel) sk.channel()),entity.getCompressCode(),ok,securityService);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            returnError();
        }
    }

    private void getServiceList(){
        log.info(this.getClass().getName(),"消息解析为[获取服务列表]");
        String services=container.getNames();
        UniversalEntity ok= UniversalEntityWrapper.getOKEntity(entity.getCompressCode());
        ok.setMessage(services);
        ExchangerUtils.Send(((SocketChannel) sk.channel()),entity.getCompressCode(),ok,securityService);
    }

    private void returnError(){
        log.info(this.getClass().getName(),"无法解析该消息,返回错误");
        UniversalEntity error= UniversalEntityWrapper.getErrorEntity();
        ExchangerUtils.Send(((SocketChannel) sk.channel()),entity.getCompressCode(),error,securityService);
    }
}
