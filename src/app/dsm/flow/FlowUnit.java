package app.dsm.flow;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FlowUnit {

    private Method checkMethod;

    private Method method;

    public FlowUnit(Method m) {
        m.setAccessible(true);
        method = m;
    }

    public FlowUnit(Method checkMethod, Method method) {
        this.checkMethod = checkMethod;
        this.method = method;
        checkMethod.setAccessible(true);
        method.setAccessible(true);
    }

    /**
     * 检查
     *
     * @param obj  obj
     * @param args
     * @return int 1-成功 -1-失败
     */
    public int check(Object obj, Object... args) {
        if (checkMethod == null) {
            return 1;
        }
        try {
            return  (int) checkMethod.invoke(obj, args);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void invoke(Object obj, Object... args) {
        try {
            method.invoke(obj, args);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
