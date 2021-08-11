package test;

import app.dsm.server.SelectorIO;
import app.dsm.server.Server;
import app.dsm.server.adapter.ListenerAdapter;
import app.dsm.server.impl.SelectorIOImpl;
import app.dsm.server.impl.ServerImpl;
import app.utils.SimpleUtils;
import app.utils.listener.IListener;
import app.utils.net.Sender;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class server {


    @Test
    public void test1(){
        testClass testClass = new testClass();

        Server server = new ServerImpl();
        server.initialize();
        server.open(testClass);


        String s = "hello";
        try {
            Thread.sleep(500);
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.bind(new InetSocketAddress("127.0.0.1",9001));
            socketChannel.connect(new InetSocketAddress("127.0.0.1",9000));
            Sender.send(socketChannel,s.getBytes(StandardCharsets.UTF_8));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        try {

            while (true) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class testClass implements IListener{

    @Override
    public void invoke(Object obj, String... args) {
        ListenerAdapter listenerAdapter = (ListenerAdapter) obj;
        byte[] data = listenerAdapter.getData();
        System.out.println(new String(data));
    }
}
