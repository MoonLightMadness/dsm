package dsm.flow.sample;

import dsm.flow.ComponentMethod;
import dsm.flow.FlowEngine;
import dsm.flow.Module;
import dsm.flow.ModuleComponent;
import dsm.flow.impl.StandardComponent;
import dsm.flow.impl.StandardFlowEngine;
import dsm.flow.impl.StandardModule;
import org.junit.Test;

/**
 * @ClassName : dsm.flow.sample.TestSample
 * @Description :
 * @Date 2021-05-07 21:27:25
 * @Author 张怀栏
 */
public class TestSample {
    @Test
    public void Test1(){
        try {
            ComponentMethod t_componentMethod=new t_ComponentMethod();

            ModuleComponent moduleComponent=new StandardComponent();
            moduleComponent.setComponentMethod(t_componentMethod);
            moduleComponent.setName("Stage_1");

            ModuleComponent moduleComponent_s2=new StandardComponent();
            moduleComponent_s2.setComponentMethod(t_componentMethod);
            moduleComponent_s2.setName("Stage_2");

            moduleComponent.setNextComponent(moduleComponent_s2);

            Module module=new StandardModule();
            module.setRootComponent(moduleComponent);
//            module.preWork();
//
//
//            module.start();
//            module.suspend();
//            Thread.sleep(10);
//            module.deSuspend();

            FlowEngine flowEngine=new StandardFlowEngine();
            flowEngine.init();
            flowEngine.addModule("t1",module);
            flowEngine.startFlow("t1",null);
            flowEngine.suspendFlow("t1");
            for (int i = 0; i < 10; i++) {
                System.out.println(i);
                Thread.sleep(1);
            }
            //flowEngine.abortFlow("t1");
            flowEngine.deSuspendFlow("t1");
            Thread.sleep(2);
            flowEngine.abortFlow("t1");
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    @Test
    public void Test2(){
        ComponentMethod objTest = new objTestMethod();
        ModuleComponent moduleComponent=new StandardComponent();
        moduleComponent.setComponentMethod(objTest);
        moduleComponent.setName("Stage_1");

        ModuleComponent moduleComponent_s2=new StandardComponent();
        moduleComponent_s2.setComponentMethod(objTest);
        moduleComponent_s2.setName("Stage_2");

        moduleComponent.setNextComponent(moduleComponent_s2);

        Module module=new StandardModule();
        module.setRootComponent(moduleComponent);
        FlowEngine flowEngine=new StandardFlowEngine();
        flowEngine.init();
        flowEngine.addModule("t1",module);
        flowEngine.startFlow("t1",0);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class t_ComponentMethod extends ComponentMethod{

    int num=0;


    @Override
    public boolean preRun() {
        System.out.println("preRun "+num++);
        return true;
    }

    @Override
    public void run() {
        System.out.println("running ");
        for (int i=0;i<10000;i++){
            num++;
        }
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postRun() {
        System.out.println("postRun "+num++);
    }

    @Override
    public void suspend() {
        System.out.println("suspend "+num++);
    }

    @Override
    public void deSuspend() {
        System.out.println("deSuspend "+num++);
    }

    @Override
    public void abort() {
        Thread.currentThread().interrupt();
    }
}
class objTestMethod extends ComponentMethod{
    @Override
    public boolean preRun() {
        return true;
    }

    @Override
    public void run() {
        try {
            int i = (int) getObj();
            System.out.println(i);
            setObj(i+=5);
            i+=5;
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}