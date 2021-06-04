package app.dsm.service.entity;

import java.nio.channels.SocketChannel;

/**
 * @ClassName : app.dsm.mq.entity.Consumer
 * @Description :
 * @Date 2021-05-28 14:27:04
 * @Author ZhangHL
 */
public class Consumer {

    private SocketChannel channel;

    private String interest;

    public SocketChannel getChannel() {
        return channel;
    }

    public void setChannel(SocketChannel channel) {
        this.channel = channel;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
