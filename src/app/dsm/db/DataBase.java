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
}
