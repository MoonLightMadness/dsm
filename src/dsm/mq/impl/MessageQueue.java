package dsm.mq.impl;

import dsm.base.BaseEntity;
import dsm.mq.IMessageQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @ClassName : dsm.mq.impl.MessageQueue
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
