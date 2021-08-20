package app.dsm.server.constant;

import app.utils.datastructure.ReflectIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * @ClassName : app.dsm.server.constant.Indicators
 * @Description :
 * @Date 2021-08-13 08:24:57
 * @Author ZhangHL
 */
public class Indicators {

    private volatile List<ReflectIndicator> reflectIndicators;



    /**
     * 初始化反射指示器
     *
     * @return
     * @author zhl
     * @date 2021-08-13 21:23
     * @version V1.0
     */
    public void initialize() {
        reflectIndicators = new ArrayList<>();
    }

    /**
     * 添加反射指示器到集合中
     *
     * @param
     * @return
     * @author zhl
     * @date 2021-08-13 21:17
     * @version V1.0
     */
    public void add(ReflectIndicator indicator) {
        synchronized (Indicators.class){
            ListIterator<ReflectIndicator> iterator = reflectIndicators.listIterator();
            iterator.add(indicator);
        }
    }

    /**
     * 添加一个反射指示器集合到原集合中
     *
     * @param list 反射指示器集合
     * @return
     * @author zhl
     * @date 2021-08-13 21:17
     * @version V1.0
     */
    public void add(List<ReflectIndicator> list) {
        synchronized (Indicators.class){
            ListIterator<ReflectIndicator> iterator = reflectIndicators.listIterator();
            while (iterator.hasNext()) {
                ReflectIndicator temp = iterator.next();
                add(temp);
            }
        }
    }

    /**
     * 在集合中删除反射指示器
     *
     * @param reflectIndicator reflec指示器
     * @return
     * @author zhl
     * @date 2021-08-13 21:18
     * @version V1.0
     */
    public void delete(ReflectIndicator reflectIndicator) {
        ListIterator<ReflectIndicator> iterator = reflectIndicators.listIterator();
        while (iterator.hasNext()) {
            ReflectIndicator temp = iterator.next();
            if (temp == reflectIndicator) {
                iterator.remove();
                break;
            }
        }
    }

    /**
     * 根据抽象路径删除反射指示器
     *
     * @param relativePath 抽象路径
     * @return
     * @author zhl
     * @date 2021-08-13 21:20
     * @version V1.0
     */
    public void delete(String relativePath) {
        ListIterator<ReflectIndicator> iterator = reflectIndicators.listIterator();
        while (iterator.hasNext()) {
            ReflectIndicator temp = iterator.next();
            if (temp.getRelativePath().equals(relativePath)) {
                iterator.remove();
                break;
            }
        }
    }

    public ReflectIndicator get(String relativePath) {
        ListIterator<ReflectIndicator> iterator = reflectIndicators.listIterator();
        while (iterator.hasNext()) {
            ReflectIndicator temp = iterator.next();
            if (temp.getRelativePath().equals(relativePath)) {
                return temp;
            }
        }
        return null;
    }

    /**
     * 返回一个集合的迭代器
     *
     * @return @return {@link ListIterator<ReflectIndicator> }
     * @author zhl
     * @date 2021-08-13 21:37
     * @version V1.0
     */
    public ListIterator<ReflectIndicator> getIterator() {
        return reflectIndicators.listIterator();
    }

}
