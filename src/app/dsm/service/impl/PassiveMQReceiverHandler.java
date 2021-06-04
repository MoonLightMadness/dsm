package app.dsm.service.impl;

import app.utils.net.impl.CallBack;

import java.nio.channels.SocketChannel;

public class PassiveMQReceiverHandler extends CallBack {




    @Override
    public void invoke(SocketChannel channel, byte[] data) {
        super.invoke(channel, data);
    }
}
