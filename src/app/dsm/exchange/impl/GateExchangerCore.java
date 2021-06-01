package app.dsm.exchange.impl;

import app.dsm.base.impl.UniversalEntity;
import app.dsm.base.impl.UniversalEntityWrapper;
import app.dsm.config.ConfigReader;
import app.dsm.config.impl.IPConfigReader;
import app.dsm.container.Container;
import app.dsm.container.impl.UniversalContainerFactory;
import app.dsm.exchange.ExchangerCore;
import app.dsm.exchange.Handler;
import app.log.LogSystem;
import app.log.LogSystemFactory;
import app.dsm.security.EntitySecurityService;
import app.dsm.security.excp.DecodeException;
import app.dsm.security.impl.*;
import app.utils.SimpleUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Gate信息交换器
 *
 * @ClassName : app.dsm.exchange.impl.GateExchangerCore
 * @Description :
 * @Date 2021-05-01 21:14:05
 * @Author ZhangHL
 */
public class GateExchangerCore implements ExchangerCore, Runnable {
    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    private ConfigReader reader;

    private Handler handler;

    private EntitySecurityService securityService;

    private LogSystem log;

    private List<byte[]> recv;

    private Container<SocketChannel> container;

    public void init() {
        log = LogSystemFactory.getLogSystem();
        log.immediatelySaveMode(true);
        log.info(this.getClass().getName(), "开始初始化");
        reader = new IPConfigReader();
        String[] config = reader.read();
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();

            serverSocketChannel.bind(SimpleUtils.isEmptyString(config[0]) ?
                    new InetSocketAddress(InetAddress.getLocalHost(), Integer.parseInt(config[1])) :
                    new InetSocketAddress(config[0], Integer.parseInt(config[1])));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            handler = new MessageHandler();
            securityService = new EntitySecurityServiceImpl();
            securityService.init(new AESEncoder(), new AESDecoder());
            recv = new ArrayList<>();
            container = UniversalContainerFactory.getContainer();
            log.info(this.getClass().getName(), "初始化完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void start() {

        while (true) {
            try {
                int nums = selector.select();
                log.info(this.getClass().getName(), "收到{}个请求", String.valueOf(nums));
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    if (key.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        log.info(this.getClass().getName(), "收到新的连接请求,ip={}",
                                socketChannel.getRemoteAddress().toString());
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ, UniversalEntityWrapper.getVisitorEntity());
                        container.add(socketChannel, socketChannel.getRemoteAddress().toString());
                    } else if (key.isReadable()) {
                        log.info(this.getClass().getName(), "开始接收--{}--并处理信息", ((SocketChannel) key.channel()).getRemoteAddress().toString());
                        messageReceiver(key);
                        log.info(this.getClass().getName(), "信息处理完毕");
                    }

                    it.remove();
                }
            } catch (IOException e) {
                log.error(this.getClass().getName(), "发生错误--{}", e.toString());
                e.printStackTrace();

            }
        }
    }

    private void messageReceiver(SelectionKey sk) {
        SocketChannel socketChannel = (SocketChannel) sk.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int size = 0;
        int count = 0;
        byte[] temp;
        while (true) {
            buffer.clear();
            try {
                size = socketChannel.read(buffer);
                count += size;
            } catch (Exception e) {
                log.info(this.getClass().getName(), "发生错误--{}", e.toString());
                try {
                    socketChannel.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                break;
            }
            if (size <= 0) {
                break;
            }
            buffer.flip();
            temp = new byte[size];
            System.arraycopy(buffer.array(), 0, temp, 0, size);
            recv.add(temp);
        }
        /**
         * 预处理接收到的信息，并做后续处理
         */
        byte[] data = mergeBytesList(count);
        recv.clear();
        messagePreHandle(data, sk);
    }


    @Override
    public void run() {
        start();
    }

    private void messagePreHandle(byte[] protoData, SelectionKey sk) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(protoData);
            ObjectInputStream ois = new ObjectInputStream(bais);
            UniversalEntity entity = (UniversalEntity) ois.readObject();
            entity= (UniversalEntity) securityService.decode(entity,entity.getCompressCode());
            log.info(this.getClass().getName(), "接收到的消息为--{}", entity.toString());
            if(securityService.verify(entity,new Sha1Encoder())){
                log.info(this.getClass().getName(),"验证通过");
                /**
                 * 调用处理机处理信息
                 */
                ((MessageHandler) handler).init(sk, entity,securityService,container);
                handler.handle();

            }
        } catch (IOException | ClassNotFoundException | DecodeException e) {
            log.error(this.getClass().getCanonicalName(), "发生错误--{}", e.toString());
            e.printStackTrace();
        }
    }

    private byte[] mergeBytesList(int count) {
        byte[] res = new byte[count];
        int pointer = 0;
        for (byte[] b : recv) {
            if (pointer >= count) {
                break;
            }
            System.arraycopy(b, 0, res, pointer, b.length);
            pointer += b.length;
        }
        return res;
    }
}
