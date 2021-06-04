package app.dsm.service.impl;

import app.dsm.base.BaseEntity;
import app.dsm.service.IMessageQueue;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @ClassName : app.dsm.mq.impl.MessageQueue
 * @Description :
 * @Date 2021-05-13 11:04:20
 * @Author ZhangHL
 */
public class MessageQueue implements IMessageQueue {

    private Queue<BaseEntity> msg;

    private int pointer;

    @Override
    public void init() {
        msg = new PriorityQueue<>();
        pointer = 0;
    }

    @Override
    public void put(BaseEntity entity) {
        msg.add(entity);
    }

    @Override
    public BaseEntity get() {
        return msg.poll();
    }

    @Override
    public int getCount() {
        return msg.size();
    }
}
