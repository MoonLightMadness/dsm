package test.fun.behaviortree.tree.impl;

import test.fun.behaviortree.tree.Attachment;

/**
 * @ClassName : test.fun.behaviortree.tree.impl.StandardAttachment
 * @Description :
 * @Date 2021-05-07 14:50:55
 * @Author ZhangHL
 */
public class StandardAttachment implements Attachment {

    private Object obj;

    @Override
    public Object getValue() {
        return obj;
    }

    @Override
    public void setValue(Object obj) {
        this.obj=obj;
    }
}
