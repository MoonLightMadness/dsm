package dsm.container.impl;

/**
 * @ClassName : dsm.container.impl.UniversalContainerFactory
 * @Description :
 * @Date 2021-05-04 17:40:05
 * @Author ZhangHL
 */
public class UniversalContainerFactory<T> {
    private static UniversalContainerImpl container;

    public static UniversalContainerImpl getContainer(){
        if(container==null){
            container=new UniversalContainerImpl<>();
            container.init();
        }
        return container;
    }
}
