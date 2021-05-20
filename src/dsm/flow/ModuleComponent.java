package dsm.flow;

import java.util.Map;

/**
 * 模板组件
 * @param
 * @author Zhang huai lan
 * @version V1.0
 **/
public interface ModuleComponent {


    /**
     * 运行前的检测
     * 返回true表示该流程能运行，反之不行
     * @return boolean
     */
    boolean preRun();

    /**
     * 运行流程
     */
    void run();

    /**
     * 该阶段结束后的工作
     */
    void postRun();

    /**
     * 开始
     */
    void start();

    /**
     * 暂停
     */
    void suspend();

    /**
     * 结束暂停
     */
    void deSuspend();

    /**
     * 中止
     */
    void abort();

    /**
     * 设置组件的方法
     *
     * @param method 方法
     */
    void setComponentMethod(ComponentMethod method);

    /**
     * 获取组件的方法
     *
     * @param index 指数
     * @return {@link ComponentMethod}
     */
    ComponentMethod getComponentMethod(int index);

    /**
     * 得到可运行方法
     *
     * @return {@link ComponentMethod}
     */
    ComponentMethod getRunnableMethod();

    /**
     * 获取方法总数
     *
     * @return int
     */
    int getMethodsCount();

    /**
     * 设置下一个组件
     *
     * @param component 组件
     */
    void setNextComponent(ModuleComponent component);

    /**
     * 是否有下一个组件
     *
     * @return boolean
     */
    boolean hasNext();

    /**
     * 得到下一个组件
     *
     * @return {@link ModuleComponent}
     */
    ModuleComponent getNextComponent();

    /**
     * 得到当前组件
     *
     * @return {@link ModuleComponent}
     */
    ModuleComponent getCurrentComponent();

    /**
     * 得到当前状态
     *
     * @return boolean
     */
    boolean getCurrentStatus();

    /**
     * 得到当前组件的名字
     *
     * @return {@link String}
     */
    String getName();

    /**
     * 设置当前组件名称
     *
     * @param name 的名字
     */
    void setName(String name);

    /**
     * 得到附件
     *
     *
     * @return {@link Map}
     */
    Map getAttachment();

    /**
     * 设置附件
     *
     *
     * @param attachment 附件
     */
    void setAttachment(Map attachment);

    /**
     * 重置计数器
     */
    void resetPointerCount();

}
