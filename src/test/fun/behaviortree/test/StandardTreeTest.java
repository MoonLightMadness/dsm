package test.fun.behaviortree.test;

import org.junit.Test;
import test.fun.behaviortree.tree.*;
import test.fun.behaviortree.tree.impl.*;

import java.lang.reflect.Field;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

/**
 * @ClassName : test.fun.behaviortree.test.StandardTreeTest
 * @Description :
 * @Date 2021-05-07 16:47:01
 * @Author ZhangHL
 */
public class StandardTreeTest {
    @Test
    public void test1(){
        Strategy strategy=new StandardStrategy();
        Tree tree=new StandardTree();
        tree.init(strategy);
        TreeShaper treeShaper=new StandardTreeShaper();
        Random random=new Random();
        Attachment attachment=new StandardAttachment();
        for (int i=0;i<30;i++){
            attachment=new StandardAttachment();
            int r=random.nextInt(100);
            System.out.print(r+" ");
            attachment.setValue(StandardObjectFactory.getNormalNode(r));
            Node node= new StandardNode();
            node.init(attachment);
            tree.add(node);
        }
        System.out.println();
        tree.shapeTree(treeShaper);
        //Show tree
        try {
            Node temp = tree.getRoot();
            Queue<Node> queue = new PriorityQueue<>();
            queue.add(temp);
            while (!queue.isEmpty()) {
                temp = queue.poll();
                Object obj = temp.getAttachment().getValue();
                Field f =obj.getClass().getDeclaredField("important");
                f.setAccessible(true);
                System.out.print(f.getInt(obj)+" ");
                Node[] nodes = temp.getChildNodes();
                for (int i = 0; i < nodes.length; i++) {
                    queue.add(nodes[i]);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
