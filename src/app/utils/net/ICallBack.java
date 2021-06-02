package app.utils.net;

import app.dsm.base.BaseEntity;

import java.nio.channels.SocketChannel;

public interface ICallBack {

    public void invoke(SocketChannel channel, byte[] data);

}
