package app.dsm.server.trigger;

import app.dsm.base.JSONTool;
import app.dsm.server.adapter.ListenerAdapter;
import app.dsm.server.constant.Indicators;
import app.dsm.server.domain.BasePath;
import app.utils.SimpleUtils;
import app.utils.datastructure.ReflectIndicator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ListIterator;

public class PathTrigger {


    private Indicators indicators;

    /**
     * 初始化
     * @return
     * @author zhl
     * @date 2021-08-13 22:31
     * @version V1.0
     */
    public void initialize(Indicators indicators){
        this.indicators = indicators;
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
        SimpleUtils.scanPackage(packageName,indicators);
    }

    /**
     * 根据路径触发指定方法
     * @param path 路径
     * @return @return {@link Object }
     * @author zhl
     * @date 2021-08-13 22:37
     * @version V1.0
     */
    public Object trigger(String path, String arg, ListenerAdapter adapter){
        ListIterator<ReflectIndicator> iterator = adapter.getSelectorIO().getIndicators().getIterator();
        while (iterator.hasNext()) {
            ReflectIndicator reflectIndicator = iterator.next();
            if(reflectIndicator.getRelativePath().equals(path)){
                try {
                    Class clazz = Class.forName(reflectIndicator.getClassPath());
                    Object obj = clazz.newInstance();
                    Field listener = obj.getClass().getDeclaredField("listenerAdapter");
                    listener.setAccessible(true);
                    listener.set(obj,adapter);
                    Method method = null;
                    Object result;
                    if(arg == null){
                        method = obj.getClass().getMethod(reflectIndicator.getMethodName(),null);
                        method.setAccessible(true);
                        result = method.invoke(obj,null);
                    }else {
                        method = obj.getClass().getMethod(reflectIndicator.getMethodName(), String.class);
                        method.setAccessible(true);
                        result = method.invoke(obj, arg);
                    }
                    return result;
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
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


}
