package app.dsm.db;
/**
 * 数据库接口
 * @param
 * @author Zhang huai lan
 * @version V1.0
 **/
public interface DataBase<T> {
    /**
     * 初始化
     * @author Zhang huai lan
     * @version V1.0
     **/
    void initialize();
    /**
     * 获取数据
     * @author Zhang huai lan
     * @version V1.0
     **/
    T get(String command);
    /**
     * 批量获取数据
     * @param 
     * @author Zhang huai lan
     * @version V1.0
     **/
    T[] gets(String command);
    /**
     * 插入数据
     * @param
     * @author Zhang huai lan
     * @version V1.0
     **/
    void insert(String command);
    /**
     * 删除
     * @param
     * @author Zhang huai lan
     * @version V1.0
     **/
    void delete(String command);
    /**
     * 修改
     * @param
     * @author Zhang huai lan
     * @version V1.0
     **/
    void update(String command);

    /**
     * @param command   命令
     * @param tableName 表名
     * @param clazz     clazz
     * @return @return {@link Object }
     * @author zhl
     * @date 2021-08-21 00:34
     * @version V1.0
     */
    Object[] getObjects(String command,String tableName,Class clazz);


    /**
     * @param command   命令
     * @param tableName 表名
     * @param clazz     clazz
     * @return @return {@link Object }
     * @author zhl
     * @date 2021-08-21 00:42
     * @version V1.0
     */
    Object getOneObject(String command,String tableName,Class clazz);


}
