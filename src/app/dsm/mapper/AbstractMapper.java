package app.dsm.mapper;

import java.util.List;

/**
 * 抽象映射器
 *
 * @author zhl
 * @date 2021-08-20 22:50
 */
public abstract class AbstractMapper {

    /**
     * 保存
     * @param object 对象
     * @return
     * @author zhl
     * @date 2021-08-20 22:51
     * @version V1.0
     */
    public abstract void save(Object object);

    /**
     * 保存多个对象
     * @param list 列表
     * @return
     * @author zhl
     * @date 2021-08-20 22:51
     * @version V1.0
     */
    public abstract void saveBatch(List<Object> list);

    /**
     * @return @return {@link Object }
     * @author zhl
     * @date 2021-08-20 22:52
     * @version V1.0
     */
    public abstract Object selectOne(Object object, Object condition);


    /**
     * @return @return {@link List<Object> }
     * @author zhl
     * @date 2021-08-20 22:52
     * @version V1.0
     */
    public abstract Object[] selectList(Object object, Object condition);


    /**
     * @param object 对象
     * @return
     * @author zhl
     * @date 2021-08-20 22:53
     * @version V1.0
     */
    public abstract void update(Object object, Object condition);

    /**
     * @param list 列表
     * @return
     * @author zhl
     * @date 2021-08-20 22:53
     * @version V1.0
     */
    public abstract void updateBatch(List<Object> list, List<Object> conditions);


}
