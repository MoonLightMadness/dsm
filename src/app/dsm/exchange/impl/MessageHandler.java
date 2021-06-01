package app.dsm.exchange.impl;

import app.dsm.base.impl.UniversalEntity;
import app.dsm.base.impl.UniversalEntityWrapper;
import app.dsm.container.Container;
import app.dsm.exchange.Handler;
import app.dsm.exchange.constant.MessageHandlerConstant;
import app.dsm.log.LogSystem;
import app.dsm.log.LogSystemFactory;
import app.dsm.security.EntitySecurityService;
import app.utils.special.DBUtils;
import app.utils.special.ExchangerUtils;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Locale;

/**
 * 内置消息处理器
 * @ClassName : app.dsm.exchange.impl.MessageHandler
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
