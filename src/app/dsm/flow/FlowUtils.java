package app.dsm.flow;

import app.dsm.flow.impl.StandardComponent;
import app.dsm.flow.impl.StandardFlowEngine;
import app.dsm.flow.impl.StandardModule;
import app.dsm.log.LogSystem;
import app.dsm.log.LogSystemFactory;
import app.dsm.log.debuger.Debuger;
import app.dsm.utils.SimpleUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

/**
 * @ClassName : app.dsm.flow.FlowGenerator
 * @Description :
 * @Date 2021-05-08 20:37:24
 * @Author ZhangHL
 */
public class FlowUtils {

    private static FlowEngine flowEngine;

    private static final LogSystem log = LogSystemFactory.getLogSystem();

    /**
     * 得到流引擎
     *
     * @return {@link FlowEngine}
     */
    public static FlowEngine getFlowEngine() {
        if (flowEngine == null) {
            generate();
        }
        return flowEngine;
    }



    private static void generate() {
        /**============================================================
         *  test_car_make=stage1:app.dsm.flow.sample.BodyMaker|&stage2:app.dsm.flow.sample.DoorMaker|&stage3:app.dsm.flow.sample.WheelMaker|&
         *
         *============================================================*/
        flowEngine = new StandardFlowEngine();
        flowEngine.init();
        Module module = null;
        ModuleComponent component = null;
        ComponentMethod method = null;
        //总的数据
        String[] flows = readConfigbyYaml();
        //单条数据
        String[] single;
        //流程名
        String header;
        //阶段名
        String stage;
        //子处理程序名
        String[] subs;
        //开始处理
        try {
            for (int i = 0; i < flows.length; i++) {
                //处理单条数据
                single = flows[i].split("=");
                header = single[0];
                module = new StandardModule();
                module.setName(header);
                flowEngine.addModule(header, module);
                component = new StandardComponent();
                module.setRootComponent(component);
                String[] stageProto = single[1].split("&");
                //处理每个阶段
                for (int p = 0; p < stageProto.length; p++) {
                    //得到阶段名
                    String[] stageProto2 = stageProto[p].split(":");
                    stage = stageProto2[0];
                    ModuleComponent tempComponent = component;
                    tempComponent.setName(stage);
                    if (p + 1 < stageProto.length) {
                        component = new StandardComponent();
                        tempComponent.setNextComponent(component);
                    }
                    //得到子程序集
                    subs = stageProto2[1].split("\\|");
                    //处理子程序集
                    for (int m = 0; m < subs.length; m++) {
                        method = (ComponentMethod) Class.forName(subs[m]).newInstance();
                        tempComponent.setComponentMethod(method);
                    }
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.error(FlowUtils.class.getName(),e.getMessage());
            Debuger.addMessage(FlowUtils.class.getName()+"---"+e.getMessage());
        } catch (InstantiationException e) {
            e.printStackTrace();
            log.error(FlowUtils.class.getName(),e.getMessage());
            Debuger.addMessage(FlowUtils.class.getName()+"---"+e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            log.error(FlowUtils.class.getName(),e.getMessage());
            Debuger.addMessage(FlowUtils.class.getName()+"---"+e.getMessage());
        }

        //处理完毕
        log.info(FlowUtils.class.getName(),"FlowEngine初始化完成");
        Debuger.addMessage("流程引擎初始化完成");
    }

    /**
     * 用指定流程名生成流程
     *
     * @param name 流程的名字
     * @return {@link String} 流程id
     */
    public static String generateByName(String name) {
        /**============================================================
         *  test_car_make=stage1:app.dsm.flow.sample.BodyMaker|&stage2:app.dsm.flow.sample.DoorMaker|&stage3:app.dsm.flow.sample.WheelMaker|&
         *
         *============================================================*/
        if(flowEngine==null){
            flowEngine=new StandardFlowEngine();
            flowEngine.init();
        }
        Module module = null;
        ModuleComponent component = null;
        ComponentMethod method = null;
        //总的数据
        String[] flows = readConfigbyYaml();
        //单条数据
        String[] single;
        //流程名
        String header = null;
        //阶段名
        String stage;
        //子处理程序名
        String[] subs;
        //开始处理
        try {
            for (int i = 0; i < flows.length; i++) {
                //处理单条数据
                single = flows[i].split("=");
                header = single[0];
                if(name.equals(header)){
                    module = new StandardModule();
                    header += SimpleUtils.getTimeStamp();
                    module.setName(header);
                    flowEngine.addModule(header, module);
                    component = new StandardComponent();
                    module.setRootComponent(component);
                    String[] stageProto = single[1].split("&");
                    //处理每个阶段
                    for (int p = 0; p < stageProto.length; p++) {
                        //得到阶段名
                        String[] stageProto2 = stageProto[p].split(":");
                        stage = stageProto2[0];
                        ModuleComponent tempComponent = component;
                        tempComponent.setName(stage);
                        if (p + 1 < stageProto.length) {
                            component = new StandardComponent();
                            tempComponent.setNextComponent(component);
                        }
                        //得到子程序集
                        subs = stageProto2[1].split("\\|");
                        //处理子程序集
                        for (int m = 0; m < subs.length; m++) {
                            method = (ComponentMethod) Class.forName(subs[m]).newInstance();
                            tempComponent.setComponentMethod(method);
                        }
                    }
                    break;
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.error(FlowUtils.class.getName(),e.getMessage());
            Debuger.addMessage(FlowUtils.class.getName()+"---"+e.getMessage());
        } catch (InstantiationException e) {
            e.printStackTrace();
            log.error(FlowUtils.class.getName(),e.getMessage());
            Debuger.addMessage(FlowUtils.class.getName()+"---"+e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            log.error(FlowUtils.class.getName(),e.getMessage());
            Debuger.addMessage(FlowUtils.class.getName()+"---"+e.getMessage());
        }

        //处理完毕
        log.info(FlowUtils.class.getName(),"FlowEngine加入流程:{}",header);
        return header;
    }

    private static String[] readConfig() {
        StringBuilder res = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("./flowconfig.yaml")));
            String temp;
            while ((temp = br.readLine()) != null) {
                if (SimpleUtils.isEmptyString(temp.trim())) {
                    continue;
                }
                temp = temp.replaceAll(" ", "");
                temp = temp.replace('=', ',');
                temp = temp.replace(System.getProperty("line.separator"), "");
                res.append(temp.trim()).append("\n");
            }
            br.close();
        } catch (FileNotFoundException ffe) {
            log.info(FlowUtils.class.getName(), "找不到配置文件--{}", ffe.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.error(FlowUtils.class.getName(),e.getMessage());
            Debuger.addMessage(FlowUtils.class.getName()+"---"+e.getMessage());
        }
        return res.toString().split("\n");
    }

    private static String[] readConfigbyYaml() {
        Yaml yaml = new Yaml();
        File file = new File("./flowconfig.yaml");
        StringBuilder res = new StringBuilder();
        try {
            //第一层
            Map<String, String> flow = yaml.load(new FileInputStream(file));
            Iterator flowIterator = flow.entrySet().iterator();
            while (flowIterator.hasNext()) {
                Map.Entry entry = (Map.Entry) flowIterator.next();
                res.append(entry.getKey()).append("=");
                //第二层
                Map<String, String> stage = (Map<String, String>) entry.getValue();
                Iterator stageIterator = stage.entrySet().iterator();
                while (stageIterator.hasNext()) {
                    //第三层
                    Map.Entry stageEntry = (Map.Entry) stageIterator.next();
                    res.append(stageEntry.getKey()).append(":");
                    Map<String, String> sub = (Map<String, String>) stageEntry.getValue();
                    Iterator subIterator = sub.entrySet().iterator();
                    while (subIterator.hasNext()) {
                        Map.Entry subEntry = (Map.Entry) subIterator.next();
                        res.append(subEntry.getValue()).append("|");
                    }
                    res.append("&");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error(FlowUtils.class.getName(),e.getMessage());
            Debuger.addMessage(FlowUtils.class.getName()+"---"+e.getMessage());
        }
        return res.toString().split("\n");
    }

}
