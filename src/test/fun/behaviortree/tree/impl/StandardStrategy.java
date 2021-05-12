package test.fun.behaviortree.tree.impl;

import test.fun.behaviortree.tree.Attachment;
import test.fun.behaviortree.tree.Node;
import test.fun.behaviortree.tree.Strategy;

import java.lang.reflect.Field;

/**
 * @ClassName : test.fun.behaviortree.tree.impl.StandardStrategy
 * @Description :
 * @Date 2021-05-07 14:50:44
 * @Author ZhangHL
 */
public class StandardStrategy implements Strategy {

    @Override
    public int compare(Node n1, Node n2) {
        Attachment a1 = n1.getAttachment();
        Attachment a2 = n2.getAttachment();

        int factor1=0,factor2=0;
        String arg="important";
        try {
            Object o1=a1.getValue();
            Object o2=a2.getValue();
            Field f1=a1.getValue().getClass().getDeclaredField(arg);
            f1.setAccessible(true);
            Field f2=a2.getValue().getClass().getDeclaredField(arg);
            f2.setAccessible(true);
            factor1=f1.getInt(o1);
            factor2=f2.getInt(o2);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        int res=0;
        if(factor1>factor2){
            res=1;
        }else if(factor1<factor2){
            res=-1;
        }
        return res;
    }
}
