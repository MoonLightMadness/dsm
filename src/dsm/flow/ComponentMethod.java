package dsm.flow;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @ClassName : dsm.flow.ComponentMethod
 * @Description :
 * @Date 2021-05-07 21:10:57
 * @Author ZhangHL
 */
public class ComponentMethod {

    private Map attachment;

    /**
     * 该组件在启动前需要做的检测
     * 若不覆盖此方法，默认返回true
     * @param
     * @return 返回true则组件开始执行，否则不执行
     * @author Zhang huai lan
     * @date 14:35 2021/5/8
     **/
    public boolean preRun() {
        return true;
    }


    public void run() {


    }


    public void postRun() {

    }




    public void suspend() {

    }


    public void deSuspend() {

    }

    public void abort() {
    }


    public Method getPreRunMethod(){
        return getMethod("preRun");
    }

    public Method getRunMethod(){
        return getMethod("run");
    }

    public Method getPostRunMethod(){
        return getMethod("postRun");
    }

    public Method getAbortMethod(){
        return getMethod("abort");
    }

    public Method getSuspendMethod(){
        return getMethod("suspend");
    }

    public Method getDeSuspendMethod(){
        return getMethod("deSuspend");
    }

    public Method getSetObjectMethod(){return getMethod("setObj",Object.class);}

    private Method getMethod(String name,Class... args){
        try {
            Method m = this.getClass().getMethod(name,args);
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Map getAttachment() {
        return attachment;
    }


    public void setAttachment(Map attachment) {
        this.attachment=attachment;
    }
}
