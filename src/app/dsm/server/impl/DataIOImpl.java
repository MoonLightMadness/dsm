package app.dsm.server.impl;

import app.dsm.server.DataIO;
import app.utils.SimpleUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class DataIOImpl implements DataIO {



    @Override
    public byte[] receiveData(SocketChannel socketChannel) {
        return SimpleUtils.receiveDataInNIO(socketChannel);
    }

    @Override
    public void sendData(byte[] data, SocketChannel socketChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.put(data);
        buffer.flip();
        try {
            socketChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
