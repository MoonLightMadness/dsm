package app.dsm.flow.impl;

import app.dsm.flow.FlowConfigReader;
import app.dsm.flow.FlowEngine;
import app.dsm.flow.Module;
import app.dsm.flow.constant.FlowStatusEnum;
import app.log.LogSystem;
import app.log.LogSystemFactory;

import java.util.*;
import java.util.concurrent.*;


/**
 * 标准流程引擎
 *
 * @author ZhangHL
 * @version 1.0.0
 * @date 2021/05/10
 * @see FlowEngine
 */
public class StandardFlowEngine implements FlowEngine {

    private LogSystem log;

    private HashMap<String, Module> modules;

    private ThreadPoolExecutor executor;

    private volatile HashMap<String,Module> runningFlow;


    @Override
    public void init() {
        log = LogSystemFactory.getLogSystem();
        modules = new HashMap<>();
        String[] config = new FlowConfigReader().read();
        executor = new ThreadPoolExecutor(Integer.parseInt(config[0]),
                Integer.parseInt(config[1]),
                Integer.parseInt(config[2]),
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        runningFlow = new HashMap<>();
    }


    @Override
    public void addModule(String id, Module module) {
        modules.put(id, module);
    }

    @Override
    public void deleteModule(String moduleId) {
        modules.remove(moduleId);
    }

    /**
     * 开启新的流程，返回该流程的流程id
     * @param
     * @return 流程id
     * @author Zhang huai lan
     * @date 10:06 2021/5/9
     **/
    @Override
    public String startFlow(String moduleId, Map attachment) {
        String id = UUID.randomUUID().toString();
        StandardModule flow = null;
        flow = (StandardModule) ((StandardModule)modules.get(moduleId));
        flow.setAttachment(attachment);
        if(flow.preWork()){
            synchronized (this){
                runningFlow.put(id,flow);
            }
            executor.execute(flow);
        }
        return id;
    }

    /**
     * 中止
     *
     * @param moduleId 模块id
     */
    @Override
    public void abortFlow(String moduleId) {
        modules.get(moduleId).abort();
    }

    /**
     * 挂起
     *
     * @param moduleId 模块id
     */
    @Override
    public void suspendFlow(String moduleId) {
        modules.get(moduleId).suspend();
    }

    /**
     * 结束挂起
     *
     * @param moduleId 模块id
     */
    @Override
    public void deSuspendFlow(String moduleId) {
        modules.get(moduleId).deSuspend();
    }

    /**
     * 完成流
     *
     * @param flowId 流id
     */
    @Override
    public void finishFlow(String flowId) {
        runningFlow.remove(flowId);
    }

    @Override
    public String getFlowStatus(String flowId) {
        Module module = runningFlow.get(flowId);
        if(module==null || module.getStage()==FlowStatusEnum.CLOSED.getMessage()){
            this.finishFlow(flowId);
            return FlowStatusEnum.CLOSED.getMessage();
        }
        return FlowStatusEnum.RUNNING.getMessage();
    }
}