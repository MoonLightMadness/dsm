package test.fun.behaviortree.tree.impl;

import test.fun.behaviortree.tree.*;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @ClassName : test.fun.behaviortree.tree.impl.StandardTree
 * @Description :
 * @Date 2021-05-07 14:50:20
 * @Author ZhangHL
 */
public class StandardTree implements Tree {

    private Strategy strategy;

    private Node root;

    private int nodesCount=0;
    @Override
    public void init(Strategy strategy) {
        this.strategy = strategy;
        root=new StandardNode();
        Attachment attachment=new StandardAttachment();
        attachment.setValue(StandardObjectFactory.getNormalNode(1));
        root.init(attachment);
    }

    @Override
    public void add(Node node) {
        //使用层级遍历
        Node temp = root;
        Queue<Node> queue = new PriorityQueue<>();
        queue.add(temp);
        while (!queue.isEmpty()) {
            temp = queue.poll();
            int comp = strategy.compare(temp, node);
            if(comp>1){
                temp.setNext(node);
                nodesCount++;
                break;
            }
            Node[] nodes = temp.getChildNodes();
            for (int i = 0; i < nodes.length; i++) {
                queue.add(nodes[i]);
            }
        }
        //若最后都没有找到符合Strategy的N父节点，则将该节点置于最后一个节点之后
        temp.setNext(node);
        nodesCount++;
    }

    @Override
    public void delete(Node node) {
        //使用层级遍历
        Node temp = root;
        Queue<Node> queue = new PriorityQueue<>();
        queue.add(temp);
        while (!queue.isEmpty()) {
            temp = queue.poll();
            int comp = strategy.compare(temp, node);
            if(comp==0){
                Node parent=temp.getPreNode();
                parent.reSetChildNode(node,null);
                nodesCount--;
            }
            Node[] nodes = temp.getChildNodes();
            for (int i = 0; i < nodes.length; i++) {
                queue.add(nodes[i]);
            }
        }
    }

    @Override
    public void shapeTree(TreeShaper treeShaper) {
        treeShaper.shapeTree(root,strategy);
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public int getNodesCount() {
        return nodesCount;
    }
}
