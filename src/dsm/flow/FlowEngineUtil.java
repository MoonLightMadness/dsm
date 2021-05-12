package dsm.flow;

import dsm.flow.constant.FlowStatusEnum;

/**
 * @ClassName : dsm.flow.FlowEngineUtil
 * @Description :
 * @Date 2021-05-09 19:38:35
 * @Author ZhangHL
 */
public class FlowEngineUtil {

    private static FlowEngine flowEngine;


    /**
     * 使用流程引擎得到对象
     *
     * @param flowName 流的名字
     * @param obj      对象
     * @return {@link Object}
     */
    public static Object getObject(String flowName,Object obj){
        FlowEngine engine = FlowUtils.getFlowEngine();
        String id = engine.startFlow(flowName,obj);
        try {
            while (!engine.getFlowStatus(id).equals(FlowStatusEnum.CLOSED.getMessage())){
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
