package test.fun.behaviortree.tree.impl;

import test.fun.behaviortree.tree.Attachment;
import test.fun.behaviortree.tree.Node;
import test.fun.behaviortree.tree.Strategy;
import test.fun.behaviortree.tree.TreeShaper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @ClassName : test.fun.behaviortree.tree.impl.StandardTreeShaper
 * @Description :
 * @Date 2021-05-07 15:50:10
 * @Author ZhangHL
 */
public class StandardTreeShaper implements TreeShaper {
    @Override
    public void shapeTree(Node root, Strategy strategy) {
        //构建哈夫曼树
        List<Node> nodes = new ArrayList<>();
        Node temp = root;
        Queue<Node> queue = new PriorityQueue<>();
        queue.add(temp);
        while (!queue.isEmpty()) {
            temp = queue.poll();
            nodes.add(temp);
            for (int i = 0; i < temp.getChildNodes().length; i++) {
                queue.add(temp.getChildNode(i));
            }
        }
        Node[] arrNodes = new Node[nodes.size()];
        int count=0;
        for(Node n :nodes){
            arrNodes[count]=n;
            count++;
        }
        //运用洗牌算法排序
        for (int i = 0; i < arrNodes.length; i++) {
            for (int p = i; p < arrNodes.length; p++) {
                if(strategy.compare(arrNodes[i],arrNodes[p])==1){
                    swap(arrNodes[i],arrNodes[p]);
                }
            }
        }
        //开始构建
        int imp1=0,imp2=0;
        try {
            Object o1=arrNodes[0].getAttachment().getValue();
            Object o2=arrNodes[1].getAttachment().getValue();
            Field f1=o1.getClass().getDeclaredField("important");
            f1.setAccessible(true);
            Field f2=o2.getClass().getDeclaredField("important");
            f2.setAccessible(true);
            imp1=f1.getInt(o1);
            imp2=f2.getInt(o2);
            Node huffNode = new StandardNode();
            Attachment attachment =new StandardAttachment();
            attachment.setValue(StandardObjectFactory.getHuffmanNode(imp1+imp2));
            huffNode.init(attachment);
            huffNode.setNext(arrNodes[0]);
            huffNode.setNext(arrNodes[1]);
            for (int i = 2; i < arrNodes.length; i++) {
                if(arrNodes.length<i+1){
                    break;
                }
                o1=huffNode.getAttachment().getValue();
                o2=arrNodes[i].getAttachment().getValue();
                f1=o1.getClass().getDeclaredField("important");
                f2=o2.getClass().getDeclaredField("important");
                f1.setAccessible(true);
                f2.setAccessible(true);
                imp1=f1.getInt(o1);
                imp2=f2.getInt(o2);
                Node temp1=huffNode;
                attachment =new StandardAttachment();
                attachment.setValue(StandardObjectFactory.getHuffmanNode(imp1+imp2));
                huffNode=new StandardNode() ;
                huffNode.init(attachment);
                huffNode.setNext(huffNode);
                huffNode.setNext(arrNodes[i]);
            }
            //root.setNext(huffNode);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private void swap(Node n1,Node n2){
        Node n3=n1;
        n1=n2;
        n2=n3;
    }
}
