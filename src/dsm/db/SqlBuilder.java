package dsm.db;

/**
 * sql 构造器
 *
 * @author ZhangHL
 * @version 1.0.0
 * @date 2021/05/10
 */
public interface SqlBuilder {

    /**
     * 设置表
     *
     * @param tablename 的表
     * @return {@link SqlBuilder}
     */
    SqlBuilder setTable(String tablename);

    /**
     * 选择
     *
     * @param selectedcolumn 被选中的行
     * @return {@link SqlBuilder}
     */
    SqlBuilder select(String selectedcolumn);

    /**
     * 更新
     *
     * @param column 列名
     * @param value  值
     * @return {@link SqlBuilder}
     */
    SqlBuilder update(String column,Object value);

    /**
     * 插入
     *
     * @param column 列
     * @param value  值
     * @return {@link SqlBuilder}
     */
    SqlBuilder insert(String column,Object value);

    /**
     * 删除
     *
     * @param column 列名
     * @param value  值
     * @return {@link SqlBuilder}
     */
    SqlBuilder delete(String column,Object value);

    /**
     *
     * 选择条件
     *
     * @param column 列名
     * @param value  值
     * @return {@link SqlBuilder}
     */
    SqlBuilder where(String column,Object value);


    /**
     * 和
     *
     * @return {@link SqlBuilder}
     */
    SqlBuilder and();

    /**
     * 或
     *
     * @return {@link SqlBuilder}
     */
    SqlBuilder or();


    /**
     * 重置
     *
     * @return {@link SqlBuilder}
     */
    SqlBuilder reset();

}
