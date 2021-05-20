package dsm.flow;

import dsm.flow.constant.FlowStatusEnum;

import java.util.Map;

/**
 * @ClassName : dsm.flow.FlowEngineUtil
 * @Description :
 * @Date 2021-05-09 19:38:35
 * @Author ZhangHL
 */
public class FlowEngineUtil {

    private static FlowEngine flowEngine;


    /**
     * 开启流程引擎并得到附件结果<br/>
     * 注意:该方法会阻塞直到该流程完成
     * @param flowName 流的名字
     * @param attachment 附件
     * @return {@link Map}
     */
    public static Map getAttachment(String flowName, Map attachment){
        FlowEngine engine = FlowUtils.getFlowEngine();
        String id = engine.startFlow(flowName,attachment);
        try {
            while (!engine.getFlowStatus(id).equals(FlowStatusEnum.CLOSED.getMessage())){
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return attachment;
    }

    /**
     * 开始流程引擎
     *
     * @param flowName   流程的名字
     * @param attachment 附件
     * @return {@link String} 流程实例id
     */
    public static String startFlow(String flowName,Map attachment){
        flowEngine = FlowUtils.getFlowEngine();
        String id = flowEngine.startFlow(flowName,attachment);
        return id;
    }

    /**
     * 根据流程id得到流程状态
     *
     * @param flowId 流程id
     * @return {@link String} 流程状态
     */
    public static String getFlowStatus(String flowId){
        return  flowEngine.getFlowStatus(flowId);
    }
}
