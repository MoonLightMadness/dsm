package app.dsm.mq;

import app.dsm.base.BaseEntity;

/**
 * 消息队列
 *
 * @author ZhangHL
 * @version 1.0.0
 * @date 2021/05/13
 */
public interface IMessageQueue {

    /**
     * 初始化
     */
    void init();

    /**
     * 入队
     *
     * @param entity 实体
     */
    void put(BaseEntity entity);

    /**
     * 出队
     *
     * @return {@link BaseEntity}
     */
    BaseEntity get();

    /**
     * 得到消息队列中消息的数量
     *
     * @return int
     */
    int getCount();
}
