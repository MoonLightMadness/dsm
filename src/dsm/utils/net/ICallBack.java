package dsm.utils.net;

import dsm.base.BaseEntity;

import java.nio.channels.SocketChannel;

public interface ICallBack {

    public void invoke(SocketChannel channel, BaseEntity entity);

}
