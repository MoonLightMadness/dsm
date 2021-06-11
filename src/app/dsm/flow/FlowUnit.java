package app.dsm.flow;

import app.log.LogSystem;
import app.log.LogSystemFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FlowUnit {

    private Method checkMethod;

    private Method method;

    private LogSystem log = LogSystemFactory.getLogSystem();

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
        } catch (InvocationTargetException | IllegalAccessException e) {
            log.error(null, e.getMessage());
            return -1;
        }
    }

    public int invoke(Object obj, Object... args) {
        try {
            method.invoke(obj, args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            log.error(null, e.getMessage());
            return - 1;
        }
        return 1;
    }
}
