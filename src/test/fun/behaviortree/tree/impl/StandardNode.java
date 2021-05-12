package test.fun.behaviortree.tree.impl;

import test.fun.behaviortree.tree.Attachment;
import test.fun.behaviortree.tree.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : test.fun.behaviortree.tree.impl.StandardNode
 * @Description :
 * @Date 2021-05-07 14:50:33
 * @Author 张怀栏
 */
public class StandardNode implements Node {

    private Attachment attachment;

    private List<Node> childs;

    private Node preNode;

    @Override
    public void init(Attachment attachment) {
        this.attachment=attachment;
        childs=new ArrayList<>();
    }

    @Override
    public void setNext(Node n) {
        childs.add(n);
        n.setPreNode(this);
    }

    @Override
    public void setPreNode(Node n) {
        this.preNode=n;
    }

    @Override
    public Node getPreNode() {
        return preNode;
    }
    @Override
    public Node[] getChildNodes() {
        Node[] nodes=new Node[childs.size()];
        int count=0;
        for(Node n :childs){
            nodes[count]=n;
            count++;
        }
        return nodes;
    }

    @Override
    public Node getChildNode(int index) {
        return childs.get(index);
    }

    @Override
    public void reSetChildNode(Node src,Node node) {
        for(Node n : childs){
            if(n==src){
                n=node;
            }
        }
        reList();
    }

    @Override
    public Attachment getAttachment() {
        return attachment;
    }

    @Override
    public void reList() {
        List<Node> newNodes=new ArrayList<>();
        for (Node n : childs){
            if(n!=null){
                newNodes.add(n);
            }
        }
        childs=newNodes;
    }
}
