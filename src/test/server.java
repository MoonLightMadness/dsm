package test;

import app.dsm.base.JSONTool;
import app.dsm.config.Configer;
import app.dsm.server.SelectorIO;
import app.dsm.server.Server;
import app.dsm.server.adapter.ListenerAdapter;
import app.dsm.server.container.ServerContainer;
import app.dsm.server.container.ServerEntity;
import app.dsm.server.domain.BasePath;
import app.dsm.server.impl.SelectorIOImpl;
import app.dsm.server.impl.ServerImpl;
import app.dsm.server.vo.GetTimeRspVO;
import app.utils.SimpleUtils;
import app.utils.listener.IListener;
import app.utils.listener.ThreadListener;
import app.utils.net.Sender;
import app.utils.special.RTimer;
import lombok.Data;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ListIterator;

public class server {


    @Test
    public void test1(){
        testClass testClass = new testClass();

        Server server = new ServerImpl();
        server.initialize();
        server.open();


        Pojo pojo = new Pojo();
        pojo.setOption("setname");
        pojo.setAttachment("pojo");
//        try {
//            Thread.sleep(500);
//            SocketChannel socketChannel = SocketChannel.open();
//            socketChannel.bind(new InetSocketAddress("127.0.0.1",9002));
//            socketChannel.connect(new InetSocketAddress("127.0.0.1",9000));
//            Thread.sleep(500);
//            Sender.send(socketChannel, JSONTool.toJson(pojo));
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        try {
//
//            while (true) {
//                Thread.sleep(1000);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void test2(){
        RTimer rTimer = new RTimer();
        rTimer.start();

        Server server = new ServerImpl();
        server.initialize();
        server.open();

        System.out.println(rTimer.end());
        rTimer.start();

        ApiPojo apiPojo = new ApiPojo();
        apiPojo.setPath("/server/setname");
        apiPojo.setName("apiPojo");

//        Configer configer = new Configer();
//        try {
//            Thread.sleep(500);
//            SocketChannel socketChannel = SocketChannel.open();
//            socketChannel.bind(new InetSocketAddress("127.0.0.1",9002));
//            socketChannel.connect(new InetSocketAddress("127.0.0.1", Integer.parseInt(configer.readConfig("port"))));
//            socketChannel.configureBlocking(false);
//            //Thread.sleep(1000);
//            Sender.send(socketChannel, JSONTool.toJson(apiPojo));
//            Thread.sleep(100);
//            System.out.println(new String(SimpleUtils.receiveDataInNIO(socketChannel)));
//            System.out.println(rTimer.end());
//            //socketChannel.close();
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }

        try {

            while (true) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class testClass implements ThreadListener {

    private Object obj;

    private String[] args;
    @Override
    public void invoke(Object obj, String... args) {
        ListenerAdapter listenerAdapter = (ListenerAdapter) obj;
        byte[] data = listenerAdapter.getData();
        if(JSONTool.getProperty("option",data) .equals("setname")){
            SelectorIOImpl selectorIO = listenerAdapter.getSelectorIO();
            ServerContainer container = selectorIO.getServerContainer();
            List<ServerEntity> serverEntities = container.getServers();
            ListIterator<ServerEntity> iterator = serverEntities.listIterator();
            while (iterator.hasNext()){
                ServerEntity serverEntity = iterator.next();
                if(serverEntity.getSocketChannel() == listenerAdapter.getChannel()){
                    serverEntity.setName(JSONTool.getProperty("attachment",data));
                }
            }
        }
        System.out.println(new String(data));
    }

    @Override
    public void setArgs(Object obj, String... args) {
        this.obj = obj;
        this.args = args;
    }


}
@Data
class Pojo{
    private String option;

    private String attachment;
}

@Data
class ApiPojo extends BasePath {

    private String name;




}
