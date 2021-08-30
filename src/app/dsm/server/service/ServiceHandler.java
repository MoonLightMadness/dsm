package app.dsm.server.service;

import app.dsm.base.JSONTool;
import app.dsm.server.adapter.ListenerAdapter;
import app.log.LogSystem;
import app.log.LogSystemFactory;

import java.lang.reflect.InvocationTargetException;

public class ServiceHandler implements Service{


    private ListenerAdapter listenerAdapter;

    private LogSystem log = LogSystemFactory.getLogSystem();

    @Override
    public void invoke(Object obj, String... args) {
        this.listenerAdapter = (ListenerAdapter) obj;
    }

    @Override
    public void setArgs(Object obj, String... args) {
        this.listenerAdapter = (ListenerAdapter) obj;
    }

    private void choose(){
        String path = JSONTool.getProperty("path",listenerAdapter.getMessagePacket().getData());
        String clazzName = path.substring(1,path.indexOf("/",1));
        String methodName = path.substring(path.lastIndexOf("/")+1);
        try {
            Class chosen = Class.forName(clazzName);
            Object obj = chosen.getDeclaredConstructor().newInstance();

        } catch (ClassNotFoundException | NoSuchMethodException e) {
            log.error("实例化类失败，原因:{}",e);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            log.error("实例化类失败，原因:{}",e);
            e.printStackTrace();
        } catch (InstantiationException e) {
            log.error("实例化类失败，原因:{}",e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            log.error("实例化类失败，原因:{}",e);
            e.printStackTrace();
        }
    }
}
