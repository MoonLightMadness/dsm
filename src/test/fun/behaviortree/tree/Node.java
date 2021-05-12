package test.fun.behaviortree.tree;

public interface Node {

    void init(Attachment attachment);

    void setNext(Node n);

    void setPreNode(Node n);

    Node getPreNode();

    Node[] getChildNodes();

    Node getChildNode(int index);

    void reSetChildNode(Node src, Node node);

    Attachment getAttachment();
    /**
     * 用于删除子节点后重新排列剩下节点
     * @param
     * @author Zhang huai lan
     * @version V1.0
     **/
    void reList();
}
