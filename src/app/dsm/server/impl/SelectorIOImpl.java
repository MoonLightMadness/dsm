package app.dsm.server.impl;

import app.dsm.config.Configer;
import app.dsm.exception.ServiceException;
import app.dsm.exception.UniversalErrorCodeEnum;
import app.dsm.server.BeatChecker;
import app.dsm.server.SelectorIO;
import app.dsm.server.adapter.ApiListenerAdapter;
import app.dsm.server.adapter.ListenerAdapter;
import app.dsm.server.constant.Indicators;
import app.dsm.server.container.ServerContainer;
import app.dsm.server.filter.Filter;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.utils.listener.ThreadListener;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@Data
public class SelectorIOImpl implements SelectorIO,Runnable {

    private Selector selector;

    private LogSystem log = LogSystemFactory.getLogSystem();

    private ThreadListener threadListener;

    private ServerContainer serverContainer;

    private BeatChecker beatChecker;

    private Filter filter;

    private Indicators indicators;

    /**
     * 正在接收数据的远端服务器
     */
    private List<SocketChannel> receivingChannels;

    @Override
    public void initialize(){
        indicators = new Indicators();
        indicators.initialize();
        Configer configer = new Configer();
        serverContainer = new ServerContainer();
        serverContainer.initialize();
        beatChecker = new BeatCheckerImpl();
        beatChecker.startBeat(serverContainer,Long.parseLong(configer.readConfig("beat.time.unit"))
                ,Integer.parseInt(configer.readConfig("beat.max")));
        threadListener = new ApiListenerAdapter();
        ((ApiListenerAdapter)threadListener).initialize(indicators);
        filter = new Filter();
        receivingChannels = new ArrayList<>();
        new Thread(beatChecker).start();
    }

    @Override
    public void open(ServerSocketChannel serverSocketChannel) {
        try {
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            log.error("Selector开启失败,原因{}",e);
            e.printStackTrace();
        }
    }



    @Override
    public void register(SocketChannel socketChannel) {
        try {
            log.info("远端服务器注册Selector开始，入参:{}",socketChannel);
            socketChannel.register(selector, SelectionKey.OP_READ);
        }catch (IOException e) {
            log.error("远端服务器注册Selector失败，原因:{}",e);
        }
    }

    @Override
    public void run() {
        try {
            log.info("开始接收数据");
            while (true){
                int num = selector.select();
                Iterator<SelectionKey> keys =  selector.selectedKeys().iterator();
                while (keys.hasNext()){
                    SelectionKey key = keys.next();
                    //远端服务请求连接
                    try {
                        if(key.isAcceptable()){
                            accept(key);
                            keys.remove();
                            continue;
                        }
                        //远端服务器发来数据
                        if(key.isReadable() && checkReceiving(key)){
                            read(key);
                        }
                    }catch (Exception e){
                        log.error("出现错误:原因:{}",e);
                    }
                    //丢弃该key
                    keys.remove();

                }
            }
        } catch (IOException e) {
            log.error("数据接收线程出现错误，原因：{}",e);
            e.printStackTrace();
        }
    }

    @SneakyThrows
    private void accept(SelectionKey key){
        try {
            log.info("远端服务器注册");
            SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
            if(filter.canPass(channel)){
                channel.configureBlocking(false);
                register(channel);
                serverContainer.add(channel);
                log.info("远端服务器注册成功,{}",channel.getRemoteAddress());
            }else {
                log.info("服务器校验不通过,抛弃该次请求");
            }
        }catch (Exception e) {
            log.error("远端服务器注册失败，原因：{}",e);
            throw new ServiceException(UniversalErrorCodeEnum.UEC_010003.getCode(), UniversalErrorCodeEnum.UEC_010003.getMsg()+e);
        }
    }

    private void read(SelectionKey key){
        try {
            log.info("Server读取远程服务器发来数据，开始，ip:{}--{}",((SocketChannel)key.channel()).getRemoteAddress(),
                    Thread.currentThread().getName());
            //将该key的channel加入到正在接收数据的channel集合中
            ListIterator<SocketChannel> iterator = receivingChannels.listIterator();
            iterator.add((SocketChannel) key.channel());
            ListenerAdapter listenerAdapter = new ListenerAdapter();
            listenerAdapter.setChannel(((SocketChannel)key.channel()));
            listenerAdapter.setSelectorIO(this);
            listenerAdapter.setThreadListener(threadListener);
            log.info("异步接收数据，开始");
            new Thread(listenerAdapter).start();
        }catch (Exception e) {
            log.error("Server读取远程服务器发来数据失败，原因：{}",e);
        }
    }

    /**
     * 检测该远程请求是否正在接收数据
     * @param key
     * @return 空闲 -> true,正在接收 -> false
     * @author zhl
     * @date 2021-08-14 15:02
     * @version V1.0
     */
    private boolean checkReceiving(SelectionKey key){
        ListIterator<SocketChannel> iterator = receivingChannels.listIterator();
        if(iterator.hasNext()){
            SocketChannel channel = iterator.next();
            SocketChannel remoter = (SocketChannel) key.channel();
            if(channel == remoter){
                return false;
            }
        }
        return true;
    }



}
