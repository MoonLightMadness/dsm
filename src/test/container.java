package test;

import dsm.container.Container;
import dsm.container.impl.UniversalContainerFactory;
import org.junit.Test;

/**
 * @ClassName : test.container
 * @Description :
 * @Date 2021-05-04 17:42:31
 * @Author ZhangHL
 */
public class container {
    @Test
    public void universalContainerTest(){
        Container<Integer> container= UniversalContainerFactory.getContainer();
        container.add(1,"a");
        container.add(2,"b");
        System.out.println(container.get("a"));
        container.delete("a");
        System.out.println(container.get("a"));
        Container test =UniversalContainerFactory.getContainer();
        System.out.println(test.get("b"));
    }
}
