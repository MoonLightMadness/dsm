package test.fun.behaviortree.tree;

public interface Strategy {
    /**
     * 比较大小,若逻辑上n1>n2,则返回1,否则返回-1，相等返回0
     * @param
     * @author Zhang huai lan
     * @version V1.0
     **/
    int compare(Node n1, Node n2);
}
