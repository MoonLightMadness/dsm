package app.dsm.server.impl;

import app.dsm.exception.ServiceException;
import app.dsm.exception.UniversalErrorCodeEnum;
import app.dsm.server.BeatChecker;
import app.dsm.server.DataIO;
import app.dsm.server.SelectorIO;
import app.dsm.server.adapter.ListenerAdapter;
import app.dsm.server.container.ServerContainer;
import app.dsm.server.container.ServerEntity;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.utils.SimpleUtils;
import app.utils.listener.IListener;
import app.utils.listener.ThreadListener;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.ListIterator;

@Data
public class SelectorIOImpl implements SelectorIO,Runnable {

    private Selector selector;

    private LogSystem log = LogSystemFactory.getLogSystem();

    private ThreadListener threadListener;

    private ServerContainer serverContainer;

    private BeatChecker beatChecker;

    @Override
    public void initialize(){
        serverContainer = new ServerContainer();
        serverContainer.initialize();
        beatChecker = new BeatCheckerImpl();
        beatChecker.startBeat(serverContainer,1000,60);
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
    public void setListener(ThreadListener threadListener) {
        this.threadListener = threadListener;
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
                    if(key.isAcceptable()){
                        accept(key);
                    }
                    //远端服务器发来数据
                    if(key.isReadable()){
                        read(key);
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
            channel.configureBlocking(false);
            register(channel);
            serverContainer.add(channel);
            log.info("远端服务器注册成功,{}",channel.getRemoteAddress());
        }catch (Exception e) {
            log.error("远端服务器注册失败，原因：{}",e);
            throw new ServiceException(UniversalErrorCodeEnum.UEC_010003.getCode(), UniversalErrorCodeEnum.UEC_010003.getMsg()+e);
        }
    }

    private void read(SelectionKey key){
        try {
            log.info("Server读取远程服务器发来数据，开始，ip:{}",((SocketChannel)key.channel()).getRemoteAddress());
            ListenerAdapter listenerAdapter = new ListenerAdapter();
            listenerAdapter.setData(SimpleUtils.receiveDataInNIO((SocketChannel) key.channel()));
            listenerAdapter.setChannel(((SocketChannel)key.channel()));
            listenerAdapter.setSelectorIO(this);
            log.info("Server读取远程服务器发来数据完成，开始触发订阅方法");
            new Thread(threadListener).start();
            log.info("订阅方法触发完成");
        }catch (Exception e) {
            log.error("Server读取远程服务器发来数据失败，原因：{}",e);
        }
    }



}
