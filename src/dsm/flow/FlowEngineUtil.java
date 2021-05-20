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
     * 开启流程引擎并得到附件结果
     *
     * @param flowName 流的名字
     * @param attachment 附件
     * @return {@link Object}
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
}
