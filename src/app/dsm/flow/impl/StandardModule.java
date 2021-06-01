package app.dsm.flow.impl;

import app.dsm.flow.Module;
import app.dsm.flow.ModuleComponent;
import app.dsm.flow.constant.FlowStatusEnum;

import java.util.Map;

/**
 * @ClassName : app.dsm.flow.impl.StandardModule
 * @Description :
 * @Date 2021-05-07 20:38:29
 * @Author ZhangHL
 */
public class StandardModule implements Module, Runnable {

    private ModuleComponent root;

    private ModuleComponent current;

    private long time;

    private boolean pause = false;

    private boolean isInterrupted=false;

    private String name;

    private Map attachment;

    @Override
    public void start() {

    }

    @Override
    public boolean preWork() {
        current = root;
        boolean canRun = root.preRun();
        if(canRun){
            current.setAttachment(attachment);
            return canRun;
        }
        //也许这里以后需要改动
        current.setAttachment(attachment);
        return canRun;
    }

    @Override
    public void work() {
        time = System.currentTimeMillis();
        current.start();
    }

    @Override
    public void postWork() {
//        if (isInterrupted) {
//            System.out.println("flow has been aorted nearby:" + current.getName());
//        } else {
//            System.out.println("flow complete");
//        }
        time = System.currentTimeMillis() - time;
        //System.out.println("Runtime:" + time + " ms");
        abort();
    }

    @Override
    public void flowToNext() {
        attachment = current.getAttachment();
        while (current.hasNext()) {
            ModuleComponent temp = current.getNextComponent();
            boolean canRun = true;
            try {
                canRun = temp.preRun();
            }catch (Exception e){
            }
            if (temp != null && canRun) {
                current = temp;
                current.setAttachment(attachment);
                return;
            }
        }
        current=null;
    }

    @Override
    public void suspend() {
        current.suspend();
        this.pause = true;
    }

    private void onPause() {
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deSuspend() {
        current.deSuspend();
        dePause();
    }

    private void dePause() {
        synchronized (this) {
            this.pause = false;
            this.notify();
        }

    }

    @Override
    public void abort() {
        isInterrupted=true;
    }

    @Override
    public void run() {
        while (current != null) {
            while (pause) {
                onPause();
            }
            if (isInterrupted) {
                this.postWork();
                return;
            }
            this.work();
            if (isInterrupted) {
                this.postWork();
                return;
            }
            this.flowToNext();
        }
        this.postWork();
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
    public ModuleComponent getRootComponent() {
        return root;
    }

    @Override
    public void setRootComponent(ModuleComponent component) {
        this.root = component;
    }

    @Override
    public String getStage() {
        if(isInterrupted){
            return FlowStatusEnum.CLOSED.getMessage();
        }else {
            return FlowStatusEnum.RUNNING.getMessage();
        }
    }

    @Override
    public Map getAttachment() {
        return attachment;
    }

    @Override
    public void setAttachment(Map attachment) {
        this.attachment=attachment;
    }



}
