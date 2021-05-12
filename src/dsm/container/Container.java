package dsm.container;

/**
 * 键值对容器
 * @param
 * @author Zhang huai lan
 * @version V1.0
 **/
public interface Container<T> {
    /**
     *
     * @param name - 即Key
     * @return
     * @author Zhang huai lan
     * @date 10:36 2021/5/7
     **/
    void add(T value,String name);

    void delete(String name);

    T get(String name);

    String getNames();

    void reName(String srcName,String destName);
}
