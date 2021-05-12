package dsm.flow;


/**
 * 流程模板
 *
 * @author ZhangHL
 * @version 1.0.0
 * @date 2021/05/10
 */
public interface Module {

    /**
     * 得到第一个阶段
     *
     * @return {@link ModuleComponent}
     */
    ModuleComponent getRootComponent();

    /**
     * 设置第一个阶段
     *
     * @param component 组件
     */
    void setRootComponent(ModuleComponent component);

    /**
     * 正式开启之前的准备工作
     *
     * @return boolean
     */
    boolean preWork();

    /**
     * 开启流程
     */
    void work();

    /**
     * 流程结束后的工作
     */
    void postWork();

    /**
     * 流向下一个
     */
    void flowToNext();

    /**
     * 暂停
     */
    void suspend();

    /**
     * 恢复暂停
     */
    void deSuspend();

    /**
     * 中止
     */
    void abort();

    /**
     * 开始
     */
    void start();

    /**
     * 得到阶段的名字
     *
     * @return {@link String}
     */
    String getName();

    /**
     * 设置阶段的名字
     *
     * @param name 的名字
     */
    void setName(String name);
    /**
     * 获取当前流程的阶段
     */
    String getStage();

    /**
     * 得到对象
     *
     * @return {@link Object}
     */
    Object getObject();

    /**
     * 设置传入的对象
     *
     * @param obj obj
     */
    void setObject(Object obj);
}
