package app.dsm.server.trigger;

import app.dsm.base.JSONTool;
import app.dsm.server.constant.Indicators;
import app.utils.SimpleUtils;
import app.utils.datastructure.ReflectIndicator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ListIterator;

public class PathTrigger {

    /**
     * 初始化
     * @return
     * @author zhl
     * @date 2021-08-13 22:31
     * @version V1.0
     */
    public void initialize(){
        Indicators.initialize();
    }

    /**
     * 搜索包
     * @param packageName 包名
     * @return
     * @author zhl
     * @date 2021-08-13 22:32
     * @version V1.0
     */
    public void scanPackage(String packageName){
        SimpleUtils.scanPackage(packageName);
    }

    /**
     * 根据路径触发指定方法
     * @param path 路径
     * @return @return {@link Object }
     * @author zhl
     * @date 2021-08-13 22:37
     * @version V1.0
     */
    public Object trigger(String path){
        ListIterator<ReflectIndicator> iterator = Indicators.getIterator();
        while (iterator.hasNext()) {
            ReflectIndicator reflectIndicator = iterator.next();
            if(reflectIndicator.getRelativePath().equals(path)){
                try {
                    Class clazz = Class.forName(reflectIndicator.getClassPath());
                    Object obj = clazz.newInstance();
                    Method method = obj.getClass().getMethod(reflectIndicator.getMethodName());
                    method.setAccessible(true);
                    return method.invoke(obj);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


}
