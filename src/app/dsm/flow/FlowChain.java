package app.dsm.flow;

import app.log.LogSystem;
import app.log.LogSystemFactory;

import java.util.ArrayList;
import java.util.List;

public class FlowChain implements Runnable {

    private LogSystem log = LogSystemFactory.getLogSystem();

    private List<FlowNode> nodes;

    private String name;

    private Object[] mt_args;

    private String mode = "default";

    public void init() {
        nodes = new ArrayList<>();
    }

    public void add(FlowNode node) {
        nodes.add(node);
    }

    public void start(Object... args) {
        FlowNode temp = null;
        Object obj = null;
        try {
            for (FlowNode node : nodes) {
                if (obj == null || !obj.getClass().getName().equals(node.getClassName())) {
                    obj = Class.forName(node.getClassName()).newInstance();
                }
                if (node.check(obj, args) == 1) {
                    temp = node;
                    node.invoke(obj, args);
                    break;
                }
            }
            if (temp != null) {
                flow(temp, obj, args);
            }
            this.nodes = null;
        } catch (ClassNotFoundException e) {
            log.error(null, e.getMessage());
        } catch (InstantiationException e) {
            log.error(null, e.getMessage());
        } catch (IllegalAccessException e) {
            log.error(null, e.getMessage());
        }
    }

    @Override
    public void run() {
        start(mt_args);
    }

    public void setArgs(Object... args) {
        mt_args = args;
    }

    public boolean checkFinish() {
        if (nodes == null) {
            return true;
        } else {
            return false;
        }
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    private void flow(FlowNode node, Object obj, Object... args) {
        while ((node = node.getNext()) != null) {
            if (node.check(obj, args) == 1) {
                int res = node.invoke(obj, args);
                if (res == -1 && mode.equals("IK")) {
                    log.error(null, "流程因异常关闭--关闭点:{}--Class:{}", node.getName(), node.getClassName());
                    break;
                }
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
