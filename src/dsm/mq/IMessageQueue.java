package dsm.mq;

import dsm.base.BaseEntity;

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
    public void init();

    /**
     * 入队
     *
     * @param entity 实体
     */
    public void put(BaseEntity entity);

    /**
     * 出队
     *
     * @return {@link BaseEntity}
     */
    public BaseEntity get();

    /**
     * 得到消息队列中消息的数量
     *
     * @return int
     */
    public int getCount();
}
