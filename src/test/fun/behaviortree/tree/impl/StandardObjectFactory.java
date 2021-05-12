package test.fun.behaviortree.tree.impl;

/**
 * @ClassName : test.fun.behaviortree.tree.impl.StandardObjectFactory
 * @Description :
 * @Date 2021-05-07 16:14:37
 * @Author ZhangHL
 */
public class StandardObjectFactory {
    public static StandardObject getOne(int important){
        StandardObject obj=new StandardObject();
        obj.setImportant(important);
        return obj;
    }

    public static StandardObject getHuffmanNode(int important){
        StandardObject obj=new StandardObject();
        obj.setImportant(important);
        obj.setLevel(0);
        obj.setName("Huffman");
        return obj;
    }

    public static StandardObject getNormalNode(int important){
        StandardObject obj=new StandardObject();
        obj.setImportant(important);
        obj.setLevel(1);
        obj.setName("Normal");
        return obj;
    }
}
