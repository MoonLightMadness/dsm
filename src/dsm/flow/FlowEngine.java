package dsm.flow;

import dsm.flow.constant.FlowStatusEnum;


/**
 * 流程引擎
 *
 * @author ZhangHL
 * @date 2021/05/10
 */
public interface FlowEngine {

    /**
     * 初始化
     */
    void init();

    /**
     * 添加模块
     *
     * @param id     id
     * @param module 模块
     */
    void addModule(String id,Module module);

    /**
     * 删除模块
     *
     * @param moduleId 模块id
     */
    void deleteModule(String moduleId);


    /**
     * 开启新的流程，返回该流程的流程id
     *
     * @param moduleId 模块id
     * @param obj      传入参数
     * @return {@link String}
     */
    String startFlow(String moduleId,Object obj);

    /**
     * 中止流程
     *
     * @param moduleId 模块id
     */
    void abortFlow(String moduleId);

    /**
     * 挂起流程
     *
     * @param moduleId 模块id
     */
    void suspendFlow(String moduleId);

    /**
     * 结束挂起
     *
     * @param moduleId 模块id
     */
    void deSuspendFlow(String moduleId);

    /**
     * 结束流程
     *
     * @param moduleId 模块id
     */
    void finishFlow(String moduleId);

    /**
     * 得到流状态
     *
     * @param moduleId 模块id
     * @return {@link String}
     */
    String getFlowStatus(String moduleId);
}
