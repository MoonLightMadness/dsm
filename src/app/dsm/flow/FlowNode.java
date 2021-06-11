package app.dsm.flow;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FlowNode {

    private FlowUnit unit;

    private FlowNode next;

    private String className;

    private String name;

    public int invoke(Object obj,Object... args){
        return unit.invoke(obj, args);
    }

    public int check(Object obj,Object... args){
        return unit.check(obj, args);
    }

    public FlowUnit getUnit() {
        return unit;
    }

    public void setUnit(FlowUnit unit) {
        this.unit = unit;
    }

    public FlowNode getNext() {
        return next;
    }

    public void setNext(FlowNode next) {
        this.next = next;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
