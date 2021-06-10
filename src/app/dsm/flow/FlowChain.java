package app.dsm.flow;

import app.log.LogSystem;
import app.log.LogSystemFactory;

import java.util.ArrayList;
import java.util.List;

public class FlowChain {

    private LogSystem log = LogSystemFactory.getLogSystem();

    private List<FlowNode> nodes;

    private String name;

    public void init(){
        nodes = new ArrayList<>();
    }

    public void add(FlowNode node) {

        nodes.add(node);
    }

    public void start(Object... args){
        FlowNode temp = null;
        Object obj = null;
        try {
            for (FlowNode node : nodes){
                if(obj == null || !obj.getClass().getName().equals(node.getClassName())){
                    obj = Class.forName(node.getClassName()).newInstance();
                }
                if(node.check(obj, args)==1){
                    temp = node;
                    node.invoke(obj, args);
                    break;
                }
            }
            if(temp!=null){
                flow(temp, obj, args);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void flow(FlowNode node,Object obj,Object... args){
        while ((node = node.getNext())!=null){
            if(node.check(obj, args)==1){
                node.invoke(obj, args);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}