package dsm.flow.impl;

import dsm.flow.ComponentMethod;
import dsm.flow.ModuleComponent;
import dsm.flow.constant.FlowStatusEnum;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName : dsm.flow.impl.StandardComponent
 * @Description :
 * @Date 2021-05-07 20:48:07
 * @Author ZhangHL
 */
public class StandardComponent implements ModuleComponent {

    private long runtime;

    private List<ModuleComponent> components;

    private int component_pointer = 0;

    private boolean flag = false;

    private List<ComponentMethod> methods;

    private ComponentMethod runnableMethod;

    private String name;

    private Map attachment;

    @Override
    public boolean preRun() {
        boolean isSuccess = false;
        if (methods == null) {
            methods = new ArrayList<>();
        }
        try {
            for (ComponentMethod method : methods) {
                isSuccess = (boolean) method.getPreRunMethod().invoke(method);
                if (isSuccess) {
                    runnableMethod = method;
                    flag = true;
                    return isSuccess;
                }
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    @Override
    public void run() {
        runnableMethod.setAttachment(attachment);
        runnableMethod.run();
//            runnableMethod.getSetObjectMethod().invoke(runnableMethod, obj);
//            runnableMethod.getRunMethod().invoke(runnableMethod);
        attachment = runnableMethod.getAttachment();
    }

    @Override
    public void postRun() {
        runnableMethod.postRun();
        //runnableMethod.getPostRunMethod().invoke(runnableMethod);
    }

    @Override
    public void start() {
        run();
        postRun();
    }

    @Override
    public void suspend() {
        runnableMethod.suspend();
        //runnableMethod.getSuspendMethod().invoke(runnableMethod);
    }

    @Override
    public void deSuspend() {
        runnableMethod.deSuspend();
        //runnableMethod.getDeSuspendMethod().invoke(runnableMethod);
    }

    @Override
    public void abort() {
        runnableMethod.abort();
        //runnableMethod.getAbortMethod().invoke(runnableMethod);
    }


    @Override
    public void setComponentMethod(ComponentMethod method) {
        if (methods == null) {
            methods = new ArrayList<>();
        }
        methods.add(method);
    }

    @Override
    public ComponentMethod getComponentMethod(int index) {
        return methods.get(index);
    }

    @Override
    public ComponentMethod getRunnableMethod() {
        return runnableMethod;
    }

    @Override
    public int getMethodsCount() {
        if (methods == null) {
            methods = new ArrayList<>();
        }
        return methods.size();
    }

    @Override
    public void setNextComponent(ModuleComponent component) {
        newComponents();
        components.add(component);
    }

    @Override
    public ModuleComponent getNextComponent() {
        newComponents();
        ModuleComponent res = null;
        if (components.isEmpty()) {
            return null;
        }
        res = components.get(component_pointer);
        res.setAttachment(this.getAttachment());
        component_pointer++;
        return res;
    }

    @Override
    public boolean hasNext() {
        newComponents();
        return component_pointer < components.size();
    }

    @Override
    public ModuleComponent getCurrentComponent() {
        return this;
    }

    @Override
    public boolean getCurrentStatus() {
        return flag;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Map getAttachment() {
        return attachment;
    }

    @Override
    public void setAttachment(Map attachment) {
        this.attachment=attachment;
    }

    /**
     * 如果components为空则new一个
     *
     * @param
     * @return
     * @author Zhang huai lan
     * @date 14:24 2021/5/8
     **/
    private void newComponents() {
        if (components == null) {
            components = new ArrayList<>();
            component_pointer = 0;
        }
    }

}
