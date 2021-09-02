package app.dsm.server.trigger;

import app.dsm.server.adapter.ListenerAdapter;
import app.dsm.server.constant.Indicators;
import app.utils.SimpleUtils;
import app.utils.datastructure.ReflectIndicator;
import app.utils.net.NetUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
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
        NetUtils.scanPackage(packageName,indicators);
    }

    /**
     * 根据路径触发指定方法
     * @param path 路径
     * @return @return {@link Object }
     * @author zhl
     * @date 2021-08-13 22:37
     * @version V1.0
     */
    public Object trigger(String path, String arg, ListenerAdapter adapter,ReflectIndicator indicator){
        if(indicator.getRelativePath().equals(path)){
            try {
                Class clazz = Class.forName(indicator.getClassPath());
                Class para = Class.forName(indicator.getParameterTypes()[0]);
                Object obj = clazz.newInstance();
                Field listener = obj.getClass().getDeclaredField("listenerAdapter");
                listener.setAccessible(true);
                listener.set(obj,adapter);
                Method method = null;
                Object result;
                method = obj.getClass().getMethod(indicator.getMethodName(), para);
                method.setAccessible(true);
                result = method.invoke(obj, SimpleUtils.parseTo(arg.getBytes(StandardCharsets.UTF_8),para));
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
        return null;
    }


}
