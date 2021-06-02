package app.dsm.base;

import app.log.LogSystem;
import app.log.LogSystemFactory;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName : app.dsm.base.JSONTool
 * @Description :
 * @Date 2021-06-02 14:28:12
 * @Author ZhangHL
 */
public class JSONTool {

    private static LogSystem log = LogSystemFactory.getLogSystem();

    public static byte[] toJson(Object obj){
        String res = JSONObject.toJSONString(obj,true);
        return res.getBytes(StandardCharsets.UTF_8);
    }

    public static Object getObject(byte[] data,Class c){
        try {
            //System.out.println(new String(data));
            JSONObject obj = (JSONObject) JSONObject.parse(new String(data));
            Field[] fs = c.getDeclaredFields();
            Object entity = c.newInstance();
            for(Field f : fs){
                f.setAccessible(true);
                String prop = obj.getString(f.getName());
                f.set(entity,prop);
            }
            return entity;
        } catch (InstantiationException e) {
            e.printStackTrace();
            log.error(null,e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            log.error(null,e.getMessage());
        }
        return null;
    }
}
