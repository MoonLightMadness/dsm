package app.utils;

import app.dsm.base.BaseEntity;

import java.lang.reflect.Field;

/**
 * @ClassName : app.utils.EntityUtils
 * @Description :
 * @Date 2021-05-06 20:07:17
 * @Author ZhangHL
 */
public class EntityUtils {

    public static BaseEntity propertiesCopy(BaseEntity entity){
        BaseEntity base=null;
        try {
            base=(entity.getClass().newInstance());
            for(Field f : entity.getClass().getDeclaredFields()){
                f.setAccessible(true);
                f.set(base,f.get(entity));
            }
            return base;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return base;
    }
}
